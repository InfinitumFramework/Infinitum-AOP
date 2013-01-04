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
import com.clarionmedia.infinitum.di.annotation.Autowired;
import com.clarionmedia.infinitum.aop.JoinPoint;
import com.clarionmedia.infinitum.context.InfinitumContext;

/**
 * <p>
 * Separates cross-cutting concerns from core application code by providing
 * pointcut advice. This annotation indicates a {@link Class} contains advice
 * functionality which can be applied to a {@link JoinPoint}.
 * </p>
 * <p>
 * {@code Aspects} are special types of beans, which, when annotated, can be
 * picked up during auto-detection and registered with the
 * {@link InfinitumContext}. Because they are beans, {@code Aspects} can have
 * {@link Autowired} members. If an bean name is not suggested for an
 * {@code Aspect}, the {@code Aspect} will be registered using the camelcase
 * version of its {@code Class} name. For example, an annotated {@code Class}
 * {@code FooAdvice} will use the bean name {@code fooAdvice} unless otherwise
 * specified.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/12/12
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Aspect {

	/**
	 * Declares the name of this {@code Aspect} bean.
	 * 
	 * @return the {@code Aspect} bean name
	 */
	String value() default "";

}
