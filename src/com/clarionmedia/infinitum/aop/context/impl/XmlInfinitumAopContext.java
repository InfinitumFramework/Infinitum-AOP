/*
 * Copyright (c) 2012 Tyler Treat
 * 
 * This file is part of Infinitum Framework.
 *
 * Infinitum Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Infinitum Framework is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Infinitum Framework.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.clarionmedia.infinitum.aop.context.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.Context;

import com.clarionmedia.infinitum.aop.AspectFactory;
import com.clarionmedia.infinitum.aop.annotation.Aspect;
import com.clarionmedia.infinitum.aop.context.InfinitumAopContext;
import com.clarionmedia.infinitum.aop.impl.AnnotationsAspectWeaver;
import com.clarionmedia.infinitum.aop.impl.ConfigurableAspectFactory;
import com.clarionmedia.infinitum.aop.impl.XmlAspectWeaver;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.RestfulContext;
import com.clarionmedia.infinitum.context.impl.XmlApplicationContext;
import com.clarionmedia.infinitum.context.impl.XmlAspect;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanDefinitionBuilder;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.di.XmlBean;
import com.clarionmedia.infinitum.di.annotation.Scope;
import com.clarionmedia.infinitum.di.impl.GenericBeanDefinitionBuilder;
import com.clarionmedia.infinitum.internal.StringUtil;

/**
 * <p>
 * Implementation of {@link InfinitumAopContext} which is initialized through
 * XML as a child of an {@link XmlApplicationContext} instance.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/24/12
 * @since 1.0
 * 
 */
public class XmlInfinitumAopContext implements InfinitumAopContext {

	private XmlApplicationContext mParentContext;
	private List<InfinitumContext> mChildContexts;
	private AspectFactory mAspectFactory;

	public XmlInfinitumAopContext(XmlApplicationContext parentContext) {
		mParentContext = parentContext;
		mChildContexts = new ArrayList<InfinitumContext>();
		mAspectFactory = new ConfigurableAspectFactory(parentContext);
	}

	@Override
	public void postProcess(Context context) {
		BeanFactory mBeanFactory = mParentContext.getBeanFactory();
		Set<XmlBean> xmlComponents = mParentContext.getXmlComponents();
		
		// Collect XML aspects
		List<XmlAspect> xmlAspects = new ArrayList<XmlAspect>();
		for (XmlBean component : xmlComponents) {
			if (XmlAspect.class.isAssignableFrom(component.getClass())) {
				xmlAspects.add((XmlAspect) component);
			}
		}
		
		// Register XML aspects
		mAspectFactory.registerAspects(xmlAspects);
		
		// Register scanned aspects
		Set<Class<?>> aspects = getAndRemoveAspects(mParentContext.getScannedComponents());
		BeanDefinitionBuilder beanDefinitionBuilder = new GenericBeanDefinitionBuilder(mBeanFactory);
		for (Class<?> aspectClass : aspects) {
			Aspect aspect = aspectClass.getAnnotation(Aspect.class);
			String beanName = aspect.value().trim().equals("") ? StringUtil.toCamelCase(aspectClass.getSimpleName()) : aspect.value()
					.trim();
			Scope scope = aspectClass.getAnnotation(Scope.class);
			String scopeVal = "singleton";
			if (scope != null)
				scopeVal = scope.value();
			AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.setName(beanName).setType(aspectClass).setProperties(null)
					.setScope(scopeVal).build();
			mAspectFactory.registerAspect(beanDefinition);
		}
		
		// Process aspects
		// Currently not supporting use of XML and annotation aspects in
		// conjunction, only one or the other right now...
		if (isComponentScanEnabled())
			new AnnotationsAspectWeaver(this).weave(mParentContext.getAndroidContext(), aspects);
		else
			new XmlAspectWeaver(this, xmlAspects).weave(mParentContext.getAndroidContext(), null);
	}

	@Override
	public Object getAspect(String name) {
		return mAspectFactory.loadAspect(name);
	}

	@Override
	public AspectFactory getAspectFactory() {
		return mAspectFactory;
	}

	@Override
	public <T> T getAspect(String name, Class<T> clazz) {
		return mAspectFactory.loadAspect(name, clazz);
	}

	@Override
	public boolean isDebug() {
		return mParentContext.isDebug();
	}

	@Override
	public Context getAndroidContext() {
		return mParentContext.getAndroidContext();
	}

	@Override
	public BeanFactory getBeanFactory() {
		return mParentContext.getBeanFactory();
	}

	@Override
	public Object getBean(String name) {
		return mParentContext.getBean(name);
	}

	@Override
	public <T> T getBean(String name, Class<T> clazz) {
		return mParentContext.getBean(name, clazz);
	}

	@Override
	public boolean isComponentScanEnabled() {
		return mParentContext.isComponentScanEnabled();
	}

	@Override
	public List<InfinitumContext> getChildContexts() {
		return mChildContexts;
	}

	@Override
	public void addChildContext(InfinitumContext context) {
		mChildContexts.add(context);
	}

	@Override
	public InfinitumContext getParentContext() {
		return mParentContext;
	}
	
	@Override
	public RestfulContext getRestContext() {
		return mParentContext.getRestContext();
	}
	
	private Set<Class<?>> getAndRemoveAspects(Collection<Class<?>> components) {
		Set<Class<?>> aspects = new HashSet<Class<?>>();
		Iterator<Class<?>> iter = components.iterator();
		while (iter.hasNext()) {
			Class<?> component = iter.next();
			if (component.isAnnotationPresent(Aspect.class)) {
				aspects.add(component);
				iter.remove();
			}
		}
		return aspects;
	}


}
