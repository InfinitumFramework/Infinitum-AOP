/*
 * Copyright (c) 2012 Tyler Treat
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clarionmedia.infinitum.aop.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.clarionmedia.infinitum.aop.AspectDefinition;
import com.clarionmedia.infinitum.aop.AspectDefinition.AdviceDefinition;
import com.clarionmedia.infinitum.aop.AspectTransformer;
import com.clarionmedia.infinitum.aop.JoinPoint.AdviceLocation;
import com.clarionmedia.infinitum.aop.annotation.After;
import com.clarionmedia.infinitum.aop.annotation.Around;
import com.clarionmedia.infinitum.aop.annotation.Aspect;
import com.clarionmedia.infinitum.aop.annotation.Before;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.context.impl.XmlAspect;
import com.clarionmedia.infinitum.internal.StringUtil;
import com.clarionmedia.infinitum.reflection.ClassReflector;
import com.clarionmedia.infinitum.reflection.PackageReflector;
import com.clarionmedia.infinitum.reflection.impl.DefaultClassReflector;
import com.clarionmedia.infinitum.reflection.impl.DefaultPackageReflector;

/**
 * <p>
 * Basic implementation of {@link AspectTransformer}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/28/12
 * @since 1.0
 */
public class GenericAspectTransformer implements AspectTransformer {

	private ClassReflector mClassReflector;
	private PackageReflector mPackageReflector;

	/**
	 * Constructs a new {@code GenericAspectTransformer} instance.
	 */
	public GenericAspectTransformer() {
		mClassReflector = new DefaultClassReflector();
		mPackageReflector = new DefaultPackageReflector();
	}

	@Override
	public AspectDefinition transform(Class<?> aspect) {
		if (!aspect.isAnnotationPresent(Aspect.class))
			throw new IllegalArgumentException("Must be Aspect-annotated class.");
		AspectDefinition ret = new AspectDefinition();
		ret.setType(aspect);
		Aspect anno = aspect.getAnnotation(Aspect.class);
		String name = anno.value() == "" ? StringUtil.toCamelCase(aspect.getSimpleName()) : anno.value();
		ret.setName(name);
		List<AdviceDefinition> adviceList = new ArrayList<AdviceDefinition>();
		List<Method> before = mClassReflector.getAllMethodsAnnotatedWith(aspect, Before.class);
		for (Method method : before) {
			AdviceDefinition advice = new AdviceDefinition();
			Before beforeAnno = method.getAnnotation(Before.class);
			advice.setMethod(method);
			advice.setType(AdviceLocation.Before);
			advice.setOrder(beforeAnno.order());
			String[] beans = beforeAnno.beans();
			String[] within = beforeAnno.within();
			if (beans != null && within != null) {
				AdviceDefinition secondAdvice = new AdviceDefinition(advice);
				secondAdvice.setPointcutType("within");
				secondAdvice.setPointcutValue(within);
				advice.setPointcutType("beans");
				advice.setPointcutValue(beans);
				adviceList.add(advice);
				adviceList.add(secondAdvice);
			} else if (beans != null) {
				advice.setPointcutType("beans");
				advice.setPointcutValue(beans);
				adviceList.add(advice);
			} else if (within != null) {
				advice.setPointcutType("within");
				advice.setPointcutValue(within);
				adviceList.add(advice);
			}
		}
		List<Method> after = mClassReflector.getAllMethodsAnnotatedWith(aspect, After.class);
		for (Method method : after) {
			AdviceDefinition advice = new AdviceDefinition();
			After afterAnno = method.getAnnotation(After.class);
			advice.setMethod(method);
			advice.setType(AdviceLocation.After);
			advice.setOrder(afterAnno.order());
			String[] beans = afterAnno.beans();
			String[] within = afterAnno.within();
			if (beans != null && within != null) {
				AdviceDefinition secondAdvice = new AdviceDefinition(advice);
				secondAdvice.setPointcutType("within");
				secondAdvice.setPointcutValue(within);
				advice.setPointcutType("beans");
				advice.setPointcutValue(beans);
				adviceList.add(advice);
				adviceList.add(secondAdvice);
			} else if (beans != null) {
				advice.setPointcutType("beans");
				advice.setPointcutValue(beans);
				adviceList.add(advice);
			} else if (within != null) {
				advice.setPointcutType("within");
				advice.setPointcutValue(within);
				adviceList.add(advice);
			}
		}
		List<Method> around = mClassReflector.getAllMethodsAnnotatedWith(aspect, Around.class);
		for (Method method : around) {
			AdviceDefinition advice = new AdviceDefinition();
			Around aroundAnno = method.getAnnotation(Around.class);
			advice.setMethod(method);
			advice.setType(AdviceLocation.Around);
			advice.setOrder(aroundAnno.order());
			String[] beans = aroundAnno.beans();
			String[] within = aroundAnno.within();
			if (beans != null && within != null) {
				AdviceDefinition secondAdvice = new AdviceDefinition(advice);
				secondAdvice.setPointcutType("within");
				secondAdvice.setPointcutValue(within);
				advice.setPointcutType("beans");
				advice.setPointcutValue(beans);
				adviceList.add(advice);
				adviceList.add(secondAdvice);
			} else if (beans != null) {
				advice.setPointcutType("beans");
				advice.setPointcutValue(beans);
				adviceList.add(advice);
			} else if (within != null) {
				advice.setPointcutType("within");
				advice.setPointcutValue(within);
				adviceList.add(advice);
			}
		}
		ret.setAdvice(adviceList);
		return ret;
	}

	@Override
	public AspectDefinition transform(XmlAspect xmlAspect) {
		AspectDefinition ret = new AspectDefinition();
		Class<?> clazz = mPackageReflector.getClass(xmlAspect.getClassName());
		ret.setType(clazz);
		ret.setName(xmlAspect.getId());
		List<AdviceDefinition> adviceList = new ArrayList<AdviceDefinition>();
		for (XmlAspect.Advice xmlAdvice : xmlAspect.getAdvice()) {
			AdviceDefinition advice = new AdviceDefinition();
			String methodName = xmlAdvice.getId();
			Method method = mClassReflector.getMethodsByName(clazz, methodName).get(0);
			advice.setMethod(method);
			String type = xmlAdvice.getType();
			if (type.equalsIgnoreCase("before"))
				advice.setType(AdviceLocation.Before);
			else if (type.equalsIgnoreCase("after"))
				advice.setType(AdviceLocation.After);
			else if (type.equalsIgnoreCase("around"))
				advice.setType(AdviceLocation.Around);
			else
				throw new InfinitumConfigurationException("Invalid advice type '" + type + "' defined in '" + xmlAspect.getId() + "'.");
			advice.setPointcutType(xmlAdvice.getPointcut());
			advice.setPointcutValue(xmlAdvice.getSeparatedValues());
			advice.setOrder(xmlAdvice.getOrder());
			adviceList.add(advice);
		}
		ret.setAdvice(adviceList);
		return ret;
	}

}
