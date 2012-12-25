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

package com.clarionmedia.infinitum.aop;

import java.util.List;
import java.util.Map;

import com.clarionmedia.infinitum.aop.annotation.Aspect;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.context.impl.XmlAspect;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;

/**
 * <p>
 * Stores aspects that have been configured in {@code infinitum.cfg.xml} or
 * through annotations. Aspects are retrieved by their name and registered by
 * providing a name, class, and field values.
 * </p>
 * <p>
 * {@code AspectFactory} is responsible for maintaining an aspect registry,
 * initializing aspect instances, and performing any necessary dependency
 * injections.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/24/12
 * @since 1.0
 */
public interface AspectFactory {

	/**
	 * Retrieves an instance of the {@link Aspect} bean with the given name. The
	 * name is configured in {@code infinitum.cfg.xml} or the {@link Aspect}
	 * annotation.
	 * 
	 * @param name
	 * @return
	 * @throws InfinitumConfigurationException
	 */
	Object loadAspect(String name) throws InfinitumConfigurationException;

	/**
	 * Retrieves an instance of the aspect with the given name and {@link Class}
	 * . The name is configured in {@code infinitum.cfg.xml} or the
	 * {@link Aspect} annotation.
	 * 
	 * @param name
	 *            the name of the aspect to retrieve
	 * @param clazz
	 *            the type of the aspect to retrieve
	 * @return an instance of the aspect
	 * @throws InfinitumConfigurationException
	 *             if the aspect does not exist, could not be constructed, or is
	 *             of the wrong type
	 */
	<T> T loadAspect(String name, Class<T> clazz) throws InfinitumConfigurationException;

	/**
	 * Checks if an aspect with the given name exists.
	 * 
	 * @param name
	 *            the name to check
	 * @return {@code true} if it exists, {@code false} if not
	 */
	boolean aspectExists(String name);

	/**
	 * Registers the aspect with the {@code AspectFactory}.
	 * 
	 * @param beanDefinition
	 *            the {@link AbstractBeanDefinition} to register
	 * 
	 */
	void registerAspect(AbstractBeanDefinition beanDefinition);

	/**
	 * Registers the given aspects with the {@code AspectFactory}.
	 * 
	 * @param aspects
	 *            the aspects to register
	 */
	void registerAspects(List<XmlAspect> aspects);

	/**
	 * Retrieves the aspect {@link Map} for this {@code AspectFactory}.
	 * 
	 * @return {@code Map} of bean names and their corresponding
	 *         {@link BeanDefinition} instances
	 */
	Map<String, AbstractBeanDefinition> getAspectDefinitions();

	/**
	 * Returns the associated {@link InfinitumContext}.
	 * 
	 * @return {@code InfinitumContext}
	 */
	InfinitumContext getContext();

}
