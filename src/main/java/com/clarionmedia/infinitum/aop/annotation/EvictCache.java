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

package com.clarionmedia.infinitum.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Indicates that the specified cache(s) will be evicted when the annotated method
 * is invoked.
 * </p>
 * <p>
 * The value for this annotation indicates the name(s) of the cache(s) to evict. If no
 * cache names are specified, all caches will be evicted.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/28/12
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EvictCache {

	/**
	 * Declares the name(s) of the cache(s) to evict.
	 * 
	 * @return cache name(s)
	 */
	String[] value() default "";

}
