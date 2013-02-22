/*
 * Copyright (C) 2012 Clarion Media, LLC
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.clarionmedia.infinitum.aop.AspectDefinition;
import com.clarionmedia.infinitum.aop.AspectDefinition.AdviceDefinition;
import com.clarionmedia.infinitum.aop.JoinPoint;
import com.clarionmedia.infinitum.aop.JoinPoint.AdviceLocation;
import com.clarionmedia.infinitum.aop.Pointcut;
import com.clarionmedia.infinitum.aop.PointcutBuilder;
import com.clarionmedia.infinitum.aop.context.InfinitumAopContext;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.exception.InfinitumRuntimeException;
import com.clarionmedia.infinitum.internal.CollectionUtil;
import com.clarionmedia.infinitum.reflection.ClassReflector;
import com.clarionmedia.infinitum.reflection.impl.JavaClassReflector;

/**
 * <p>
 * Basic implementation of {@link PointcutBuilder}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/28/12
 * @since 1.0
 */
public class GenericPointcutBuilder implements PointcutBuilder {

	private ClassReflector mClassReflector;
	private InfinitumAopContext mContext;
	private BeanFactory mBeanFactory;

	/**
	 * Constructs a new {@code GenericPointcutBuilder} instance.
	 * 
	 * @param context
	 *            {@link InfinitumAopContext}
	 */
	public GenericPointcutBuilder(InfinitumAopContext context) {
		mClassReflector = new JavaClassReflector();
		mContext = context;
		mBeanFactory = context.getBeanFactory();
	}

	@Override
	public Collection<Pointcut> build(Collection<AspectDefinition> aspects) {
		Map<String, Pointcut> pointcutMap = new HashMap<String, Pointcut>();
		for (AspectDefinition aspect : aspects) {
			Object advisor = mClassReflector.getClassInstance(aspect.getType());
			for (AdviceDefinition advice : aspect.getAdvice())
				processAdvice(advisor, advice, pointcutMap);
		}
		return pointcutMap.values();
	}

	private void processAdvice(Object advisor, AdviceDefinition advice, Map<String, Pointcut> pointcutMap) {
		if (advice.getPointcutType().equalsIgnoreCase("beans"))
			processBeanJoinPoints(advisor, advice, pointcutMap);
		else if (advice.getPointcutType().equalsIgnoreCase("within"))
			processWithinJoinPoints(advisor, advice, pointcutMap);
	}

	// Processes JoinPoints specified by the "beans" attribute
	// e.g. @Before(beans = { "fooBean", "barBean.method(*)" })
	private void processBeanJoinPoints(Object advisor, AdviceDefinition advice, Map<String, Pointcut> pointcutMap) {
		for (String bean : advice.getPointcutValue()) {
			bean = bean.trim();
			if (bean.length() == 0)
				continue;
			String beanName = bean;
			boolean isClassScope = false;
			if (bean.contains("."))
				beanName = bean.substring(0, bean.indexOf('.'));
			else
				isClassScope = true;
			Class<?> beanType = mBeanFactory.getBeanType(beanName);
			if (!advice.qualifies(beanType))
				continue;
			Object beanObject = mBeanFactory.loadBean(beanName);
			JoinPoint joinPoint = advice.getType() == AdviceLocation.Around ? new BasicProceedingJoinPoint(mContext, advisor,
					advice.getMethod()) : new BasicJoinPoint(mContext, advisor, advice.getMethod(), advice.getType());
			joinPoint.setBeanName(beanName);
			joinPoint.setTarget(beanObject);
			joinPoint.setOrder(advice.getOrder());
			if (isClassScope) {
				joinPoint.setClassScope(true);
				putJoinPoint(pointcutMap, joinPoint);
			} else {
				// It's a specific method or methods matcher
				processBeanMethodJoinPoint(bean, beanObject, advisor, advice, joinPoint, pointcutMap);
			}
		}
	}

	// Processes JoinPoints specified by the "within" attribute
	// e.g. @Around(within = {"com.foo.bar.service", "com.foo.bar.dao"})
	private void processWithinJoinPoints(Object advisor, AdviceDefinition advice, Map<String, Pointcut> pointcutMap) {
		for (String pkg : advice.getPointcutValue()) {
			pkg = pkg.toLowerCase(Locale.getDefault()).trim();
			if (pkg.length() == 0)
				continue;
			boolean all = pkg.equals("*");
			Map<AbstractBeanDefinition, String> invertedMap = CollectionUtil.invert(mBeanFactory.getBeanDefinitions());
			for (AbstractBeanDefinition bean : invertedMap.keySet()) {
				if (all || bean.getType().getName().startsWith(pkg)) {
					Class<?> beanType = bean.getType();
					if (!advice.qualifies(beanType))
						continue;
					JoinPoint joinPoint = advice.getType() == AdviceLocation.Around ? new BasicProceedingJoinPoint(mContext, advisor,
							advice.getMethod()) : new BasicJoinPoint(mContext, advisor, advice.getMethod(), advice.getType());
					joinPoint.setBeanName(bean.getName());
					joinPoint.setTarget(bean.getNonProxiedBeanInstance());
					joinPoint.setOrder(advice.getOrder());
					joinPoint.setClassScope(true);
					putJoinPoint(pointcutMap, joinPoint);
				}
			}
		}
	}

	// Processes JoinPoints specified by the "beans" attribute which indicate
	// methods to advise
	// e.g. @Before(beans = { "barBean.method(*)" })
	private void processBeanMethodJoinPoint(String bean, Object beanObject, Object advisor, AdviceDefinition advice, JoinPoint joinPoint,
			Map<String, Pointcut> pointcutMap) {
		if (!bean.endsWith(")"))
			throw new InfinitumRuntimeException("Invalid join point '" + bean + "' in aspect '" + advisor.getClass().getName() + "'.");
		String methodName;
		String[] args;
		try {
			methodName = bean.substring(bean.indexOf('.') + 1, bean.indexOf('('));
			String params = bean.substring(bean.indexOf('(') + 1, bean.indexOf(')'));
			if (params.trim().length() == 0)
				args = new String[0];
			else
				args = params.split(",");
		} catch (IndexOutOfBoundsException e) {
			throw new InfinitumRuntimeException("Invalid join point '" + bean + "' in aspect '" + advisor.getClass().getName() + "'.");
		}
		if (args.length == 0) {
			// Parameterless method
			Method method = mClassReflector.getMethod(beanObject.getClass(), methodName);
			if (method == null)
				throw new InfinitumRuntimeException("Method '" + methodName + "' from pointcut '" + bean + "' could not be found.");
			joinPoint.setMethod(method);
			putJoinPoint(pointcutMap, joinPoint);
		} else if (args[0].trim().equals("*")) {
			// Wildcard -- add all methods with the given name
			for (Method method : mClassReflector.getMethodsByName(beanObject.getClass(), methodName)) {
				JoinPoint copied = advice.getType() == AdviceLocation.Around ? new BasicProceedingJoinPoint(
						(BasicProceedingJoinPoint) joinPoint) : new BasicJoinPoint((BasicJoinPoint) joinPoint);
				copied.setMethod(method);
				putJoinPoint(pointcutMap, copied);
			}
		} else {
			// Add method with the given arguments
			Class<?>[] argTypes = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				argTypes[i] = mClassReflector.getClass(args[i].trim());
			}
			Method method = mClassReflector.getMethod(beanObject.getClass(), methodName, argTypes);
			if (method == null)
				throw new InfinitumRuntimeException("Method '" + methodName + "' from pointcut '" + bean + "' could not be found.");
			joinPoint.setMethod(method);
			putJoinPoint(pointcutMap, joinPoint);
		}
	}

	// Adds the JoinPoint to a Pointcut in pointcutMap
	// If there's no Pointcut for the type, it will add one
	private void putJoinPoint(Map<String, Pointcut> pointcutMap, JoinPoint joinPoint) {
		
		if (pointcutMap.containsKey(joinPoint.getBeanName())) {
			pointcutMap.get(joinPoint.getBeanName()).addJoinPoint(joinPoint);
		} else {
			Pointcut pointcut = new Pointcut(joinPoint.getBeanName(), joinPoint.getTargetType());
			pointcut.addJoinPoint(joinPoint);
			pointcutMap.put(joinPoint.getBeanName(), pointcut);
		}
	}

}
