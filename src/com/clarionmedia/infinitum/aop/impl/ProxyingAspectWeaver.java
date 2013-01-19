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

import java.util.Set;

import android.content.Context;

import com.clarionmedia.infinitum.aop.AdvisedProxyFactory;
import com.clarionmedia.infinitum.aop.AspectDefinition;
import com.clarionmedia.infinitum.aop.AspectWeaver;
import com.clarionmedia.infinitum.aop.Pointcut;
import com.clarionmedia.infinitum.aop.PointcutBuilder;
import com.clarionmedia.infinitum.di.AbstractProxy;
import com.clarionmedia.infinitum.di.BeanFactory;

/**
 * <p>
 * Implementation of of {@link AspectWeaver} which uses proxies to advise
 * objects.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/28/12
 * @since 1.0
 */
public class ProxyingAspectWeaver implements AspectWeaver {

	private PointcutBuilder mPointcutBuilder;
	private BeanFactory mBeanFactory;
	private AdvisedProxyFactory mProxyFactory;

	/**
	 * Constructs a new {@code ProxyingAspectWeaver} instance.
	 * 
	 * @param beanFactory
	 *            the {@link BeanFactory} the aspects are scoped to
	 * @param pointcutBuilder
	 *            {@link PointcutBuilder} to use
	 * @param proxyFactory
	 *            the {@link AdvisedProxyFactory} to use
	 */
	public ProxyingAspectWeaver(BeanFactory beanFactory, PointcutBuilder pointcutBuilder, AdvisedProxyFactory proxyFactory) {
		mPointcutBuilder = pointcutBuilder;
		mProxyFactory = proxyFactory;
		mBeanFactory = beanFactory;
	}

	@Override
	public void weave(Context context, Set<AspectDefinition> aspects) {
		for (Pointcut pointcut : mPointcutBuilder.build(aspects)) {
			String beanName = pointcut.getBeanName();
			Object bean = mBeanFactory.loadBean(beanName);
			AbstractProxy proxy = mProxyFactory.createProxy(context, bean, pointcut);
			mBeanFactory.getBeanDefinitions().get(beanName).setBeanProxy(proxy);
		}
	}

}
