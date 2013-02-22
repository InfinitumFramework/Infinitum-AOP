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
