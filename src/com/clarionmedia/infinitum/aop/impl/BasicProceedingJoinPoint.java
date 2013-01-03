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

package com.clarionmedia.infinitum.aop.impl;

import java.lang.reflect.Method;

import com.clarionmedia.infinitum.aop.AbstractJoinPoint;
import com.clarionmedia.infinitum.aop.ProceedingJoinPoint;
import com.clarionmedia.infinitum.aop.annotation.Aspect;
import com.clarionmedia.infinitum.aop.context.InfinitumAopContext;
import com.clarionmedia.infinitum.internal.Preconditions;

/**
 * <p>
 * Basic implementation of {@link ProceedingJoinPoint}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/14/12
 * @since 1.0
 */
public class BasicProceedingJoinPoint extends AbstractJoinPoint implements ProceedingJoinPoint {

	private ProceedingJoinPoint mNext;

	/**
	 * Creates a new {@code BasicProceedingJoinPoint}.
	 * 
	 * @param context
	 *            the {@link InfinitumAopContext} this {@code JoinPoint} is
	 *            scoped to
	 * @param advisor
	 *            the {@link Aspect} containing the advice to apply
	 * @param advice
	 *            the advice {@link Method} to apply at this {@link JoinPoint}
	 */
	public BasicProceedingJoinPoint(InfinitumAopContext context, Object advisor, Method advice) {
		super(context, advisor, advice);
	}

	/**
	 * Creates a new {@code BasicProceedingJoinPoint} by copying from the given
	 * {@code BasicProceedingJoinPoint}.
	 * 
	 * @param joinPoint
	 *            the {@code BasicProceedingJoinPoint} to copy
	 */
	public BasicProceedingJoinPoint(BasicProceedingJoinPoint joinPoint) {
		super(joinPoint.mContext, joinPoint.mAdvisor, joinPoint.mAdvice);
		mNext = joinPoint.mNext;
	}

	@Override
	public Method getMethod() {
		return mMethod;
	}

	@Override
	public Object[] getArguments() {
		return mArguments;
	}

	@Override
	public Object getTarget() {
		return mTarget;
	}

	@Override
	public Class<?> getTargetType() {
		if (mTarget == null)
			return null;
		return mTarget.getClass();
	}

	@Override
	public void setBeanName(String beanName) {
		mBeanName = beanName;
	}

	@Override
	public String getBeanName() {
		return mBeanName;
	}

	@Override
	public void setMethod(Method method) {
		mMethod = method;
		ProceedingJoinPoint next = next();
		if (next != null)
			next.setMethod(method);
	}

	@Override
	public void setArguments(Object[] args) {
		mArguments = args;
		ProceedingJoinPoint next = next();
		if (next != null)
			next.setArguments(args);
	}

	@Override
	public void setTarget(Object target) {
		mTarget = target;
	}

	@Override
	public boolean isClassScope() {
		return mIsClassScope;
	}

	@Override
	public void setClassScope(boolean isClassScope) {
		mIsClassScope = isClassScope;
	}

	@Override
	public AdviceLocation getLocation() {
		return AdviceLocation.Around;
	}

	@Override
	public void setLocation(AdviceLocation location) {
		// Does this even make sense?
	}

	@Override
	public Object invoke() throws Exception {
		Preconditions.checkNotNull(mAdvisor);
		Preconditions.checkNotNull(mAdvice);
		return mAdvice.invoke(mAdvisor, this);
	}

	@Override
	public Object proceed() throws Exception {
		ProceedingJoinPoint next = next();
		if (next == null)
			return mMethod.invoke(mTarget, mArguments);
		return next.invoke();
	}

	@Override
	public void setNext(ProceedingJoinPoint next) {
		mNext = next;
	}

	@Override
	public ProceedingJoinPoint next() {
		return mNext;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if ((object == null) || (object.getClass() != getClass()))
			return false;
		if (object.getClass() != this.getClass())
			return false;
		BasicProceedingJoinPoint other = (BasicProceedingJoinPoint) object;
		return other.mNext == other.mNext && super.equals(other);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash *= 31 + (mNext == null ? 0 : mNext.hashCode());
		hash *= 31 + super.hashCode();
		return hash;
	}

}
