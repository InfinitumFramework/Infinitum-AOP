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

package com.clarionmedia.infinitum.aop.context;

import com.clarionmedia.infinitum.aop.AspectFactory;
import com.clarionmedia.infinitum.context.InfinitumContext;

/**
 * <p>
 * {@code InfinitumAopContext} is an extension of {@link InfinitumContext} that
 * contains configuration information for the framework AOP module.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/24/12
 * @since 1.0
 */
public interface InfinitumAopContext extends InfinitumContext {

	/**
	 * Retrieves an aspect with the given name. Aspects are configured in
	 * {@code infinitum.cfg.xml}.
	 * 
	 * @param name
	 *            the name of the aspect to retrieve
	 * @return an aspect instance or {@code null} if no aspect has been
	 *         configured with the given name
	 */
	Object getAspect(String name);

	/**
	 * Retrieves the {@link AspectFactory} for this {@code InfinitumAopContext}.
	 * The {@code AspectFactory} is used to retrieve aspects that have been
	 * configured in {@code infinitum.cfg.xml} or through component scanning.
	 * 
	 * @return {@code AspectFactory}
	 */
	AspectFactory getAspectFactory();

	/**
	 * Retrieves an aspect with the given name and {@link Class}. Aspects are
	 * configured in {@code infinitum.cfg.xml} or through component scanning.
	 * 
	 * @param name
	 *            the name of the aspect to retrieve
	 * @param clazz
	 *            the type of the aspect to retrieve
	 * @return an instance of the aspect
	 */
	<T> T getAspect(String name, Class<T> clazz);

}
