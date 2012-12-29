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

import com.clarionmedia.infinitum.context.impl.XmlAspect;

/**
 * <p>
 * Provides an API for transforming a non-generic aspect definition, i.e. an
 * aspect defined in XML or through component scanning, into a generic
 * {@link AspectDefinition}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/28/12
 * @since 1.0
 */
public interface AspectTransformer {

	/**
	 * Transforms the given aspect {@link Class} into a {@link AspectDefinition}
	 * .
	 * 
	 * @param aspect
	 *            the aspect to transform
	 * @return transformed {@code AspectDefinition}
	 */
	AspectDefinition transform(Class<?> aspect);

	/**
	 * Transforms the given {@link XmlAspect} into a {@link AspectDefinition}.
	 * 
	 * @param aspect
	 *            the aspect to transform
	 * @return transformed {@code AspectDefinition}
	 */
	AspectDefinition transform(XmlAspect aspect);

}
