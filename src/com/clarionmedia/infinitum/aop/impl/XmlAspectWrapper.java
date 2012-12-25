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

import java.util.ArrayList;
import java.util.List;

import com.clarionmedia.infinitum.aop.JoinPoint.AdviceLocation;
import com.clarionmedia.infinitum.aop.JoinPoint.PointcutType;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.context.impl.XmlAspect;

/**
 * <p>
 * Wrapper for {@link XmlAspect} which adds additional AOP functionality.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/24/12
 * @since 1.0
 * 
 */
public class XmlAspectWrapper {

	private XmlAspect mAspect;
	private List<XmlAdviceWrapper> mAdvice;

	public XmlAspectWrapper(XmlAspect aspect) {
		mAspect = aspect;
		mAdvice = new ArrayList<XmlAdviceWrapper>();
		for (XmlAspect.Advice advice : aspect.getAdvice())
			mAdvice.add(new XmlAdviceWrapper(advice));
	}

	public XmlAspect unwrap() {
		return mAspect;
	}

	public List<XmlAdviceWrapper> getAdvice() {
		return mAdvice;
	}

	public String getClassName() {
		return mAspect.getClassName();
	}

	/**
	 * <p>
	 * Wrapper for {@link XmlAspect.Advice} which adds additional AOP
	 * functionality.
	 * </p>
	 * 
	 * @author Tyler Treat
	 * @version 1.0 12/24/12
	 * @since 1.0
	 * 
	 */
	public class XmlAdviceWrapper {

		private XmlAspect.Advice mAdvice;
		private String mType;

		public XmlAdviceWrapper(XmlAspect.Advice advice) {
			mAdvice = advice;
			mType = advice.getType();
		}

		public String getId() {
			return mAdvice.getId();
		}

		public String[] getSeparatedValues() {
			return mAdvice.getSeparatedValues();
		}

		public int getOrder() {
			return mAdvice.getOrder();
		}

		/**
		 * Indicates if the {@code Advice} is an {@code around} type.
		 * 
		 * @return {@code true} if it is {@code around}, {@code false} if not
		 */
		public boolean isAround() {
			return mType.equalsIgnoreCase(AdviceLocation.Around.name());
		}

		/**
		 * Returns the {@link AdviceLocation} for this {@code Advice}.
		 * 
		 * @return {@code AdviceLocation}
		 */
		public AdviceLocation getLocation() {
			if (mType.equalsIgnoreCase(AdviceLocation.Around.name()))
				return AdviceLocation.Around;
			if (mType.equalsIgnoreCase(AdviceLocation.Before.name()))
				return AdviceLocation.Before;
			if (mType.equalsIgnoreCase(AdviceLocation.After.name()))
				return AdviceLocation.After;
			throw new InfinitumConfigurationException("Unknown advice type '" + mType + "'.");
		}

		/**
		 * Returns the {@link PointcutType} for this {@code Advice}.
		 * 
		 * @return {@code PointcutType}
		 */
		public PointcutType getPointcutType() {
			String pointcut = mAdvice.getPointcut();
			if (pointcut.equalsIgnoreCase(PointcutType.Beans.name()))
				return PointcutType.Beans;
			if (pointcut.equalsIgnoreCase(PointcutType.Within.name()))
				return PointcutType.Within;
			throw new InfinitumConfigurationException("Unknown pointcut type '" + pointcut + "'.");
		}

	}
}
