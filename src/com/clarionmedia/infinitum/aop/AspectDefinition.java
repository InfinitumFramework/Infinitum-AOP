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

package com.clarionmedia.infinitum.aop;

import java.lang.reflect.Method;
import java.util.List;

import com.clarionmedia.infinitum.aop.JoinPoint.AdviceLocation;

/**
 * <p>
 * Generic aspect definition which stores the information needed to perform AOP.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/28/12
 * @since 1.0
 */
public class AspectDefinition {

	private Class<?> mType;
	private String mName;
	private List<AdviceDefinition> mAdvice;

	public Class<?> getType() {
		return mType;
	}

	public void setType(Class<?> type) {
		mType = type;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public List<AdviceDefinition> getAdvice() {
		return mAdvice;
	}

	public void setAdvice(List<AdviceDefinition> advice) {
		mAdvice = advice;
	}

	/**
	 * <p>
	 * Encapsulates a generic aspect advice.
	 * </p>
	 * 
	 * @author Tyler Treat
	 * @version 1.0 12/28/12
	 * @since 1.0
	 */
	public static class AdviceDefinition {

		private Method mMethod;
		private AdviceLocation mType;
		private String mPointcutType;
		private String[] mPointcutValue;
		private int mOrder;
		private AdviceQualifier mQualifier;

		public AdviceDefinition() {
			mOrder = Integer.MAX_VALUE;
		}

		public AdviceDefinition(AdviceDefinition advice) {
			mMethod = advice.mMethod;
			mType = advice.mType;
			mPointcutType = advice.mPointcutType;
			mPointcutValue = advice.mPointcutValue;
			mOrder = advice.mOrder;
		}

		public Method getMethod() {
			return mMethod;
		}

		public void setMethod(Method method) {
			mMethod = method;
		}

		public AdviceLocation getType() {
			return mType;
		}

		public void setType(AdviceLocation type) {
			mType = type;
		}

		public String getPointcutType() {
			return mPointcutType;
		}

		public void setPointcutType(String pointcutType) {
			mPointcutType = pointcutType;
		}

		public String[] getPointcutValue() {
			return mPointcutValue;
		}

		public void setPointcutValue(String[] pointcutValue) {
			mPointcutValue = pointcutValue;
		}

		public int getOrder() {
			return mOrder;
		}

		public void setOrder(int order) {
			mOrder = order;
		}

		public boolean qualifies(Class<?> clazz) {
			if (clazz == null)
				return false;
			if (mQualifier == null)
				return true;
			return mQualifier.qualifies(clazz);
		}

		public void setQualifier(AdviceQualifier qualifier) {
			mQualifier = qualifier;
		}

		/**
		 * <p>
		 * Used to determine if a {@link Class} qualifies for the containing
		 * {@link AdviceDefinition}.
		 * </p>
		 * 
		 * @author Tyler Treat
		 * @version 1.0 12/29/12
		 * @since 1.0
		 */
		public interface AdviceQualifier {

			/**
			 * Indicates if the given {@link Class} qualifies for the containing
			 * {@link AdviceDefinition}.
			 * 
			 * @param clazz
			 *            the {@code Class} to qualify
			 * @return {@code true} if it qualifies, {@code false} if not
			 */
			boolean qualifies(Class<?> clazz);

		}

	}

}
