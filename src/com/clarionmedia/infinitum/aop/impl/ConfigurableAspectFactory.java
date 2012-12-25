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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clarionmedia.infinitum.aop.AspectFactory;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.context.impl.XmlAspect;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.di.impl.GenericBeanDefinitionBuilder;
import com.clarionmedia.infinitum.reflection.PackageReflector;
import com.clarionmedia.infinitum.reflection.impl.DefaultPackageReflector;

/**
 * <p>
 * Implementation of {@link AspectFactory}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/24/12
 * @since 1.0
 */
public class ConfigurableAspectFactory implements AspectFactory {

	private PackageReflector mPackageReflector;
	private Map<String, AbstractBeanDefinition> mAspectDefinitions;
	private InfinitumContext mContext;
	private BeanFactory mBeanFactory;

	/**
	 * Constructs a new {@code ConfigurableAspectFactory}.
	 * 
	 * @param context
	 *            the parent {@link InfinitumContext}
	 */
	public ConfigurableAspectFactory(InfinitumContext context) {
		mContext = context;
		mPackageReflector = new DefaultPackageReflector();
		mAspectDefinitions = new HashMap<String, AbstractBeanDefinition>();
		mBeanFactory = context.getBeanFactory();
	}

	@Override
	public Object loadAspect(String name) throws InfinitumConfigurationException {
		if (!mAspectDefinitions.containsKey(name))
			throw new InfinitumConfigurationException("Aspect '" + name + "' could not be resolved");
		return mAspectDefinitions.get(name).getBeanInstance();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T loadAspect(String name, Class<T> clazz) throws InfinitumConfigurationException {
		Object aspect = loadAspect(name);
		if (!clazz.isInstance(aspect))
			throw new InfinitumConfigurationException("Aspect '" + name + "' was not of type '" + clazz.getName() + "'.");
		return (T) aspect;
	}

	@Override
	public void registerAspect(AbstractBeanDefinition beanDefinition) {
		mAspectDefinitions.put(beanDefinition.getName(), beanDefinition);
	}

	@Override
	public Map<String, AbstractBeanDefinition> getAspectDefinitions() {
		return mAspectDefinitions;
	}

	@Override
	public InfinitumContext getContext() {
		return mContext;
	}

	@Override
	public boolean aspectExists(String name) {
		return mAspectDefinitions.containsKey(name);
	}

	@Override
	public void registerAspects(List<XmlAspect> aspects) {
		if (aspects == null)
			return;
		for (XmlAspect aspect : aspects) {
			Map<String, Object> propertiesMap = new HashMap<String, Object>();
			for (XmlAspect.Property property : aspect.getProperties()) {
				String name = property.getName();
				String ref = property.getRef();
				if (ref != null) {
					propertiesMap.put(name, mBeanFactory.loadBean(ref));
				} else {
					String value = property.getValue();
					propertiesMap.put(name, value);
				}
			}
			Class<?> clazz = mPackageReflector.getClass(aspect.getClassName());
			AbstractBeanDefinition beanDefinition = new GenericBeanDefinitionBuilder(mBeanFactory).setName(aspect.getId()).setType(clazz)
					.setProperties(propertiesMap).setScope(aspect.getScope()).build();
			registerAspect(beanDefinition);
		}
	}

}
