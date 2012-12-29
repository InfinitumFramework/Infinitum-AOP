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

import java.util.Collection;

/**
 * <p>
 * Provides an API for constructing {@link Pointcut} definitions from an
 * {@link AspectDefinition}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/28/12
 * @since 1.0
 */
public interface PointcutBuilder {

	/**
	 * Builds a collection of {@link Pointcut} definitions from the given set of
	 * {@code AspectDefinition}s.
	 * 
	 * @param aspects
	 *            the {@code AspectDefinition}s to build {@code Pointcut}s for
	 * @return collection of {@code Pointcut}s
	 */
	Collection<Pointcut> build(Collection<AspectDefinition> aspects);

}
