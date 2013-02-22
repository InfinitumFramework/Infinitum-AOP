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
import com.clarionmedia.infinitum.aop.JoinPoint;
import com.clarionmedia.infinitum.aop.ProceedingJoinPoint;

/**
 * <p>
 * Indicates that the annotated advice is to be executed around a
 * {@link JoinPoint}. A specialized {@link ProceedingJoinPoint} will be passed
 * as an argument to methods annotated with this.
 * </p>
 * <p>
 * {@code Around} advice has the ability to prevent a {@code JoinPoint} from
 * being executed by returning its own return value or throwing an exception.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/12/12
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Around {

	/**
	 * Declares the beans and, optionally, specific methods which make up a
	 * pointcut.
	 * 
	 * @return array of bean names, which may or may not include specific
	 *         methods to create a pointcut
	 */
	String[] beans() default {};

	/**
	 * Declares the packages such that any contained type's methods make up a
	 * pointcut.
	 * 
	 * @return array of package names to create a pointcut
	 */
	String[] within() default {};
	
	/**
	 * Declares the advice precedence. A smaller number indicates a higher
	 * precedence, while a larger number indicates a lower precedence. The
	 * default value is {@link Integer#MAX_VALUE}. The precedence determines the
	 * order in which advice is executed.
	 * 
	 * @return the advice precedence
	 */
	int order() default Integer.MAX_VALUE;

}
