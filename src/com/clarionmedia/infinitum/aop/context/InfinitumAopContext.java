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

import java.util.Map;

import com.clarionmedia.infinitum.context.BeanProvider;
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
public interface InfinitumAopContext extends InfinitumContext, BeanProvider {

	/**
	 * Indicates if cache abstraction is enabled.
	 * 
	 * @return {@code true} if enabled, {@code false} if not
	 */
	boolean isCacheAbstractionEnabled();

	/**
	 * Retrieves the method cache used for cache abstraction.
	 * 
	 * @return method cache
	 */
	Map<String, Map<Integer, Object>> getMethodCache();

}
