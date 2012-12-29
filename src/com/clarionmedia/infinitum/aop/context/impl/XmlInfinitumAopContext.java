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

import com.clarionmedia.infinitum.aop.AspectDefinition;
import com.clarionmedia.infinitum.aop.AspectTransformer;
import com.clarionmedia.infinitum.aop.annotation.Aspect;
import com.clarionmedia.infinitum.aop.context.InfinitumAopContext;
import com.clarionmedia.infinitum.aop.impl.GenericAspectTransformer;
import com.clarionmedia.infinitum.aop.impl.GenericAspectWeaver;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.RestfulContext;
import com.clarionmedia.infinitum.context.impl.XmlApplicationContext;
import com.clarionmedia.infinitum.context.impl.XmlAspect;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.di.XmlBean;

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

	/**
	 * Creates a new {@code XmlInfinitumAopContext} instance as a child of the
	 * given {@link XmlApplicationContext}.
	 * 
	 * @param parentContext
	 *            the parent of this context
	 */
	public XmlInfinitumAopContext(XmlApplicationContext parentContext) {
		mParentContext = parentContext;
		mChildContexts = new ArrayList<InfinitumContext>();
	}

	@Override
	public void postProcess(Context context) {
		// Load aspect data from parent context
		Set<XmlBean> xmlComponents = mParentContext.getXmlComponents();
		Set<Class<?>> scannedAspects = getAndRemoveAspects(mParentContext.getScannedComponents());

		// Transform aspects from XML or scanned form to generic definition
		Set<AspectDefinition> aspects = transformAspects(xmlComponents, scannedAspects);

		// Process aspects
		new GenericAspectWeaver(getBeanFactory()).weave(context, aspects);
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
	
	private Set<AspectDefinition> transformAspects(Set<XmlBean> xmlComponents, Set<Class<?>> scannedAspects) {
		AspectTransformer transformer = new GenericAspectTransformer();
		Set<AspectDefinition> aspects = new HashSet<AspectDefinition>();

		// Transform XML aspects
		for (XmlBean component : xmlComponents) {
			if (XmlAspect.class.isAssignableFrom(component.getClass())) {
				aspects.add(transformer.transform((XmlAspect) component));
			}
		}

		// Transform scanned aspects
		for (Class<?> aspect : scannedAspects)
			aspects.add(transformer.transform(aspect));

		return aspects;
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
