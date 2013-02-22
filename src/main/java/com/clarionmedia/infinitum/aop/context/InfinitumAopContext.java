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

package com.clarionmedia.infinitum.aop.context;

import java.util.Map;

import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.di.BeanProvider;

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
