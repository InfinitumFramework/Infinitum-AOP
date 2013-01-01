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

/**
 * <p>
 * Provides support for around advice by exposing the
 * {@link ProceedingJoinPoint#proceed()} method.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/14/12
 * @since 1.0
 */
public interface ProceedingJoinPoint extends JoinPoint {

	/**
	 * Proceed with the next advice or target method invocation.
	 * 
	 * @return the value returned by the target method, if any
	 * @throws Exception
	 *             if the next advice or target method threw an exception
	 */
	Object proceed() throws Exception;

	/**
	 * Sets the subsequent {@link ProceedingJoinPoint} to invoke after this one.
	 * 
	 * @param next
	 *            the next {@code ProceedingJoinPoint}
	 */
	void setNext(ProceedingJoinPoint next);

	/**
	 * Returns the subsequent {@link PRoceedingJoinPoint} to be invoked after
	 * this one.
	 * 
	 * @return the next {@code ProceedingJoinPoint}
	 */
	ProceedingJoinPoint next();

}
