/*
 * Copyright (c) 2012 Tyler Treat
 * 
 * This file is part of Infinitum Framework.
 *
 * Infinitum Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Infinitum Framework is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Infinitum Framework.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.clarionmedia.infinitum.aop.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import android.content.Context;

import com.clarionmedia.infinitum.aop.JoinPoint;
import com.clarionmedia.infinitum.aop.Pointcut;
import com.clarionmedia.infinitum.aop.ProceedingJoinPoint;
import com.clarionmedia.infinitum.di.DexMakerProxy;
import com.clarionmedia.infinitum.internal.Preconditions;

/**
 * <p>
 * Implementation of {@link DexMakerProxy} that provides AOP advice support for
 * DexMaker-based proxies.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/13/12
 * @since 1.0
 */
public class AdvisedDexMakerProxy extends DexMakerProxy {

	private List<JoinPoint> mBeforeAdvice;
	private List<JoinPoint> mAfterAdvice;
	private ProceedingJoinPoint mAroundAdvice;
	private Pointcut mPointcut;

	/**
	 * Creates a new {@code AdvisedDexMakerProxy}.
	 * 
	 * @param context
	 *            the {@link Context} used to retrieve the DEX bytecode cache
	 * @param target
	 *            the proxied {@link Object}
	 * @param pointcut
	 *            the {@link Pointcut} to provide advice
	 */
	public AdvisedDexMakerProxy(Context context, Object target,
			Pointcut pointcut) {
		super(context, target);
		Preconditions.checkNotNull(pointcut);
		mPointcut = pointcut;
		mBeforeAdvice = new ArrayList<JoinPoint>();
		mAfterAdvice = new ArrayList<JoinPoint>();
		ProceedingJoinPoint next = null;
		Queue<JoinPoint> joinPoints = pointcut.getJoinPoints();
		while (joinPoints.size() > 0) {
			JoinPoint joinPoint = joinPoints.remove();
			switch (joinPoint.getLocation()) {
				case Before :
					mBeforeAdvice.add(joinPoint);
					break;
				case After :
					mAfterAdvice.add(joinPoint);
					break;
				case Around :
					ProceedingJoinPoint proceedingJoinPoint = (ProceedingJoinPoint) joinPoint;
					if (next != null)
						proceedingJoinPoint.setNext(next);
					next = proceedingJoinPoint;
					break;
			}
		}
		mAroundAdvice = next;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		for (JoinPoint joinPoint : mBeforeAdvice) {
			if (applies(joinPoint, method)) {
				joinPoint.setMethod(method);
				joinPoint.setArguments(args);
				joinPoint.invoke();
			}
		}
		Object ret;
		if (mAroundAdvice == null || !applies(mAroundAdvice, method)) {
			ret = method.invoke(mTarget, args);
		} else {
			mAroundAdvice.setMethod(method);
			mAroundAdvice.setArguments(args);
			ret = mAroundAdvice.invoke();
		}
		for (JoinPoint joinPoint : mAfterAdvice) {
			if (applies(joinPoint, method)) {
				joinPoint.setMethod(method);
				joinPoint.setArguments(args);
				joinPoint.invoke();
			}
		}
		return ret;
	}
	
	/**
	 * Indicates if the given {@link JoinPoint} applies to the given
	 * {@link Method}.
	 * 
	 * @param joinPoint
	 *            the {@code JoinPoint} to check
	 * @param method
	 *            the {@code Method} to check
	 * @return {@code true} if it applies, {@code false} if not
	 */
	protected boolean applies(JoinPoint joinPoint, Method method) {
		if (joinPoint.isClassScope())
			return true;
		Method joinPointMethod = joinPoint.getMethod();
		if (joinPointMethod == null)
			return false;
		if (!joinPointMethod.getName().equals(method.getName()))
			return false;
		String t1 = "";
		for (Class<?> c : joinPointMethod.getParameterTypes()) {
			t1 += c.getName() + "/";
		}
		String t2 = "";
		for (Class<?> c : method.getParameterTypes()) {
			t2 += c.getName() + "/";
		}
		if (!t1.equals(t2))
			return false;
		return true;
	}
	
	@Override
	public AdvisedDexMakerProxy clone() {
		return new AdvisedDexMakerProxy(mContext, mTarget, mPointcut);
	}

}
