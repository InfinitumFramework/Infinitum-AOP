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

import java.lang.reflect.Method;
import java.util.Comparator;

import com.clarionmedia.infinitum.aop.annotation.Aspect;
import com.clarionmedia.infinitum.aop.context.InfinitumAopContext;

/**
 * <p>
 * Provides contextual information about a join point. A {@code JoinPoint} is
 * passed to an {@link Aspect} advice when invoked to provide it with reflective
 * state data.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/12/12
 */
public interface JoinPoint {

	/**
	 * Describes the location of advice relative to the join point.
	 */
	public static enum AdviceLocation {
		Before, After, Around
	};

	/**
	 * Describes the type of pointcuts and how it's applied.
	 */
	public static enum PointcutType {
		Beans, Within
	}

	/**
	 * Returns the {@link Method} being invoked at this {@code JoinPoint}.
	 * 
	 * @return {@code Method} to be advised
	 */
	Method getMethod();

	/**
	 * Sets the {@link Method} to be invoked at this {@code JoinPoint}.
	 * 
	 * @param method
	 *            {@code Method} to be advised
	 */
	void setMethod(Method method);

	/**
	 * Returns the arguments to be passed into this {@code JoinPoint}.
	 * 
	 * @return {@code Object[]} of arguments
	 */
	Object[] getArguments();

	/**
	 * Sets the arguments to be passed into this {@code JoinPoint}.
	 * 
	 * @param args
	 *            {@code Object[]} of arguments
	 */
	void setArguments(Object[] args);

	/**
	 * Returns the target {@link Object} to be invoked at this {@code JoinPoint}
	 * .
	 * 
	 * @return {@code Object} where {@code JoinPoint} is to be executed
	 */
	Object getTarget();

	/**
	 * Sets the target {@link Object} to be invoked at this {@code JoinPoint}.
	 * 
	 * @param target
	 *            {@code Object} where {@code JoinPoint} is to be executed
	 */
	void setTarget(Object target);

	/**
	 * Returns the {@link Class} of the {@code JoinPoint} target.
	 * 
	 * @return {@code Class} of target
	 */
	Class<?> getTargetType();

	/**
	 * Returns the name of the bean to be invoked at this {@code JoinPoint}.
	 * 
	 * @return bean name
	 */
	String getBeanName();

	/**
	 * Sets the name of the bean to be invoked at this {@code JoinPoint}.
	 * 
	 * @param beanName
	 *            bean name
	 */
	void setBeanName(String beanName);

	/**
	 * Indicates if the {@code JoinPoint} applies to the entire target
	 * {@link Class} or just a specific {@link Method}.
	 * 
	 * @return {@code true} if it is {@code Class} scope, {@code false} if not
	 */
	boolean isClassScope();

	/**
	 * Sets the value indicating if the {@code JoinPoint} applies to the entire
	 * target {@link Class} or just a specific {@link Method}.
	 * 
	 * @param isClassScope
	 *            {@code true} if it is {@code Class} scope, {@code false} if
	 *            not
	 */
	void setClassScope(boolean isClassScope);

	/**
	 * Returns the advice location.
	 * 
	 * @return advice location
	 */
	AdviceLocation getLocation();

	/**
	 * Sets the advice location.
	 * 
	 * @param location
	 *            advice location
	 */
	void setLocation(AdviceLocation location);

	/**
	 * Returns the {@link Aspect} containing the advice to apply.
	 * 
	 * @return {@Aspect}
	 */
	Object getAdvisor();

	/**
	 * Sets the {@link Aspect} containing the advice to apply.
	 * 
	 * @param advisor
	 *            {@code Aspect}
	 */
	void setAdvisor(Object advisor);

	/**
	 * Returns the advice {@link Method}.
	 * 
	 * @return advice {@code Method}
	 */
	Method getAdvice();

	/**
	 * Sets the advice {@link Method}.
	 * 
	 * @param advice
	 *            the advice {@code Method} to set
	 */
	void setAdvice(Method advice);

	/**
	 * Returns the advice precedence. A smaller number indicates a higher
	 * precedence, while a larger number indicates a lower precedence. The
	 * default value is {@link Integer#MAX_VALUE} The precedence determines the
	 * order in which advice is executed.
	 * 
	 * @return advice precedence
	 */
	int getOrder();

	/**
	 * Sets the advice precedence. A smaller number indicates a higher
	 * precedence, while a larger number indicates a lower precedence. The
	 * default value is {@link Integer#MAX_VALUE} The precedence determines the
	 * order in which advice is executed.
	 * 
	 * @param order
	 */
	void setOrder(int order);

	/**
	 * Executes the advice.
	 * 
	 * @return the advice return value, if any
	 * @throws Exception
	 *             if the advice throws any exceptions
	 */
	Object invoke() throws Exception;

	/**
	 * Returns the {@link InfinitumAopContext} this {@code JoinPoint} is scoped
	 * to.
	 * 
	 * @return {@code InfinitumAopContext}
	 */
	InfinitumAopContext getContext();

	/**
	 * <p>
	 * {@link Comparator} implementation used to compare {@code JoinPoints}
	 * based on their precedence.
	 * </p>
	 * 
	 * @author Tyler Treat
	 * @version 1.0 07/15/12
	 * @since 1.0
	 */
	public static class JoinPointComparator implements Comparator<JoinPoint> {

		@Override
		public int compare(JoinPoint lhs, JoinPoint rhs) {
			if (lhs.getOrder() == rhs.getOrder())
				return 0;
			if (lhs.getOrder() > rhs.getOrder())
				return 1;
			return -1;
		}

	}

}
