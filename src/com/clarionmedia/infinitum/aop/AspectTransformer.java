/*
 * Copyright (c) 2012 Tyler Treat
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
