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

import java.lang.reflect.Method;

import com.clarionmedia.infinitum.aop.annotation.Aspect;
import com.clarionmedia.infinitum.aop.context.InfinitumAopContext;
import com.clarionmedia.infinitum.aop.impl.BasicJoinPoint;
import com.clarionmedia.infinitum.aop.impl.BasicProceedingJoinPoint;
import com.clarionmedia.infinitum.internal.Preconditions;

/**
 * <p>
 * Abstract implementation of {@link JoinPoint}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/14/12
 * @since 1.0
 * @see BasicJoinPoint
 * @see BasicProceedingJoinPoint
 */
public abstract class AbstractJoinPoint implements JoinPoint {

	protected Object mTarget;
	protected Method mMethod;
	protected Object[] mArguments;
	protected String mBeanName;
	protected boolean mIsClassScope;
	protected Method mAdvice;
	protected Object mAdvisor;
	protected int mOrder;
	protected InfinitumAopContext mContext;

	/**
	 * Creates a new {@code AbstractJoinPoint}.
	 * 
	 * @param context
	 *            the {@link InfinitumAopContext} this {@code JoinPoint} is
	 *            scoped to
	 * @param advisor
	 *            the {@link Aspect} containing the advice to apply
	 * @param advice
	 *            the advice {@link Method} to apply at this {code JoinPoint}
	 */
	public AbstractJoinPoint(InfinitumAopContext context, Object advisor, Method advice) {
		Preconditions.checkNotNull(advisor);
		Preconditions.checkNotNull(advice);
		Preconditions.checkNotNull(context);
		mAdvisor = advisor;
		mAdvice = advice;
		mAdvice.setAccessible(true);
		mContext = context;
	}

	/**
	 * Creates a new {@code AbstractJoinPoint} by copying from the given
	 * {@code AbstractJoinPoint}.
	 * 
	 * @param joinPoint
	 *            the {@code AbstractJoinPoint} to copy
	 */
	public AbstractJoinPoint(AbstractJoinPoint joinPoint) {
		mAdvisor = joinPoint.mAdvisor;
		mAdvice = joinPoint.mAdvice;
		mArguments = joinPoint.mArguments;
		mBeanName = joinPoint.mBeanName;
		mIsClassScope = joinPoint.mIsClassScope;
		mMethod = joinPoint.mMethod;
		mOrder = joinPoint.mOrder;
		mTarget = joinPoint.mTarget;
		mContext = joinPoint.mContext;
	}

	@Override
	public Object getAdvisor() {
		return mAdvisor;
	}

	@Override
	public void setAdvisor(Object advisor) {
		mAdvisor = advisor;
	}

	@Override
	public Method getAdvice() {
		return mAdvice;
	}

	@Override
	public void setAdvice(Method advice) {
		mAdvice = advice;
	}

	@Override
	public int getOrder() {
		return mOrder;
	}

	@Override
	public void setOrder(int order) {
		mOrder = order;
	}

	@Override
	public InfinitumAopContext getContext() {
		return mContext;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if ((object == null) || (!getClass().isAssignableFrom(object.getClass())))
			return false;
		AbstractJoinPoint other = (AbstractJoinPoint) object;
		return other.mIsClassScope == mIsClassScope && other.mAdvice.equals(mAdvice) && other.mAdvisor.equals(mAdvisor)
				&& other.mArguments.equals(mArguments) && other.mBeanName.equals(mBeanName) && other.mMethod.equals(mMethod)
				&& other.mTarget.equals(mTarget);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash *= 31 + (mTarget == null ? 0 : mTarget.hashCode());
		hash *= 31 + (mMethod == null ? 0 : mMethod.hashCode());
		hash *= 31 + (mArguments == null ? 0 : mArguments.hashCode());
		hash *= 31 + (mBeanName == null ? 0 : mBeanName.hashCode());
		hash *= 31 + (mIsClassScope ? 1 : 0);
		hash *= 31 + (mAdvice == null ? 0 : mAdvice.hashCode());
		hash *= 31 + (mAdvisor == null ? 0 : mAdvisor.hashCode());
		return hash;
	}
}
