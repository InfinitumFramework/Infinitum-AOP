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

package com.clarionmedia.infinitum.aop.impl;

import java.util.Set;

import android.content.Context;

import com.clarionmedia.infinitum.aop.AdvisedProxyFactory;
import com.clarionmedia.infinitum.aop.AspectDefinition;
import com.clarionmedia.infinitum.aop.AspectWeaver;
import com.clarionmedia.infinitum.aop.Pointcut;
import com.clarionmedia.infinitum.aop.PointcutBuilder;
import com.clarionmedia.infinitum.aop.context.InfinitumAopContext;
import com.clarionmedia.infinitum.di.AopProxy;
import com.clarionmedia.infinitum.di.BeanFactory;

/**
 * <p>
 * Basic implementation of {@link AspectWeaver}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/28/12
 * @since 1.0
 */
public class GenericAspectWeaver implements AspectWeaver {

	private PointcutBuilder mPointcutBuilder;
	private BeanFactory mBeanFactory;
	private AdvisedProxyFactory mProxyFactory;

	/**
	 * Constructs a new {@code GenericAspectWeaver} instance.
	 * 
	 * @param beanFactory
	 *            the {@link BeanFactory} the aspects are scoped to
	 */
	public GenericAspectWeaver(InfinitumAopContext context) {
		mPointcutBuilder = new GenericPointcutBuilder(context);
		mProxyFactory = new DelegatingAdvisedProxyFactory();
		mBeanFactory = context.getBeanFactory();
	}

	@Override
	public void weave(Context context, Set<AspectDefinition> aspects) {
		for (Pointcut pointcut : mPointcutBuilder.build(aspects)) {
			String beanName = pointcut.getBeanName();
			Object bean = mBeanFactory.loadBean(beanName);
			AopProxy proxy = mProxyFactory.createProxy(context, bean, pointcut);
			mBeanFactory.getBeanDefinitions().get(beanName).setBeanProxy(proxy);
		}
	}

}
