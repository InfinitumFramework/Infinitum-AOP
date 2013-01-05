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

package com.clarionmedia.infinitum.aop.context.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;

import com.clarionmedia.infinitum.aop.AspectDefinition;
import com.clarionmedia.infinitum.aop.AspectDefinition.AdviceDefinition;
import com.clarionmedia.infinitum.aop.AspectDefinition.AdviceDefinition.AdviceQualifier;
import com.clarionmedia.infinitum.aop.AspectTransformer;
import com.clarionmedia.infinitum.aop.JoinPoint;
import com.clarionmedia.infinitum.aop.JoinPoint.AdviceLocation;
import com.clarionmedia.infinitum.aop.ProceedingJoinPoint;
import com.clarionmedia.infinitum.aop.annotation.Aspect;
import com.clarionmedia.infinitum.aop.annotation.Cache;
import com.clarionmedia.infinitum.aop.annotation.EvictCache;
import com.clarionmedia.infinitum.aop.context.InfinitumAopContext;
import com.clarionmedia.infinitum.aop.impl.CacheAspect;
import com.clarionmedia.infinitum.aop.impl.GenericAspectTransformer;
import com.clarionmedia.infinitum.aop.impl.GenericAspectWeaver;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.RestfulContext;
import com.clarionmedia.infinitum.context.impl.XmlApplicationContext;
import com.clarionmedia.infinitum.context.impl.XmlAspect;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanDefinitionBuilder;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.di.XmlBean;
import com.clarionmedia.infinitum.internal.StringUtil;
import com.clarionmedia.infinitum.reflection.ClassReflector;
import com.clarionmedia.infinitum.reflection.impl.JavaClassReflector;

/**
 * <p>
 * Implementation of {@link InfinitumAopContext} which is initialized through
 * XML as a child of an {@link XmlApplicationContext} instance.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/24/12
 * @since 1.0
 */
public class XmlInfinitumAopContext implements InfinitumAopContext {

	private XmlApplicationContext mParentContext;
	private List<InfinitumContext> mChildContexts;
	private Map<String, Map<Integer, Object>> mMethodCache;

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
		mMethodCache = new HashMap<String, Map<Integer, Object>>();
	}

	@Override
	public void postProcess(Context context) {
		// Load aspect data from parent context
		Set<XmlBean> xmlComponents = mParentContext.getXmlComponents();
		Set<Class<?>> scannedAspects = getAndRemoveAspects(mParentContext.getScannedComponents());

		// Transform aspects from XML or scanned form to generic definition
		Set<AspectDefinition> aspects = transformAspects(xmlComponents, scannedAspects);

		// Add caching advice for cache abstraction
		if (isCacheAbstractionEnabled())
			addCachingAdvice(aspects);

		// Process aspects
		new GenericAspectWeaver(this).weave(context, aspects);
	}

	@Override
	public List<AbstractBeanDefinition> getBeans(BeanDefinitionBuilder beanDefinitionBuilder) {
		return new ArrayList<AbstractBeanDefinition>(0);
	}

	@Override
	public boolean isCacheAbstractionEnabled() {
		if (mParentContext.getAppConfig().containsKey("methodCaching"))
			return Boolean.parseBoolean(mParentContext.getAppConfig().get("methodCaching"));
		return false;
	}

	@Override
	public Map<String, Map<Integer, Object>> getMethodCache() {
		return mMethodCache;
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
	public <T extends InfinitumContext> T getChildContext(Class<T> contextType) {
		return mParentContext.getChildContext(contextType);
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

	private void addCachingAdvice(Set<AspectDefinition> aspects) {
		final ClassReflector reflector = new JavaClassReflector();
		AspectDefinition cachingAspect = new AspectDefinition();
		cachingAspect.setName(StringUtil.toCamelCase(CacheAspect.class.getSimpleName()));
		cachingAspect.setType(CacheAspect.class);
		List<AdviceDefinition> adviceList = new ArrayList<AdviceDefinition>();
		
		// Create Cache advice
		AdviceDefinition cacheAdvice = new AdviceDefinition();
		cacheAdvice.setType(AdviceLocation.Around);
		cacheAdvice.setPointcutType("within");
		cacheAdvice.setPointcutValue(new String[] { "*" });
		Method method = reflector.getMethod(CacheAspect.class, "cache", ProceedingJoinPoint.class);
		cacheAdvice.setMethod(method);
		cacheAdvice.setQualifier(new AdviceQualifier() {
			@Override
			public boolean qualifies(Class<?> clazz) {
				return reflector.containsMethodAnnotation(clazz, Cache.class);
			}
		});
		adviceList.add(cacheAdvice);
		
		// Create EvictCache advice
		AdviceDefinition evictCacheAdvice = new AdviceDefinition();
		evictCacheAdvice.setType(AdviceLocation.Before);
		evictCacheAdvice.setPointcutType("within");
		evictCacheAdvice.setPointcutValue(new String[] { "*" });
		method = reflector.getMethod(CacheAspect.class, "evictCache", JoinPoint.class);
		evictCacheAdvice.setMethod(method);
		evictCacheAdvice.setQualifier(new AdviceQualifier() {
			@Override
			public boolean qualifies(Class<?> clazz) {
				return reflector.containsMethodAnnotation(clazz, EvictCache.class);
			}
		});
		adviceList.add(evictCacheAdvice);
		
		cachingAspect.setAdvice(adviceList);
		aspects.add(cachingAspect);
	}

}
