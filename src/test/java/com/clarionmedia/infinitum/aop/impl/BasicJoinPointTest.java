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

package com.clarionmedia.infinitum.aop.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.clarionmedia.infinitum.aop.JoinPoint;
import com.clarionmedia.infinitum.aop.JoinPoint.AdviceLocation;
import com.clarionmedia.infinitum.aop.context.InfinitumAopContext;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class BasicJoinPointTest {
	
	private BasicJoinPoint joinPoint;
	private AdviceLocation location = AdviceLocation.Before;
	private MockAspect mockAdvisor;
	private Method method;
	private Object[] args;
	private String beanName;
	private Object target;
	private InfinitumAopContext mockContext = mock(InfinitumAopContext.class);
	
	@Before
	public void setup() throws NoSuchMethodException, SecurityException {
		mockAdvisor = new MockAspect();
		args = new Object[0];
		beanName = "someBean";
		target = new Object();
		method = mockAdvisor.getClass().getMethod("advice", JoinPoint.class);
		joinPoint = new BasicJoinPoint(mockContext, mockAdvisor, method, location);
		joinPoint.setArguments(args);
		joinPoint.setBeanName(beanName);
		joinPoint.setMethod(method);
		joinPoint.setTarget(target);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor() {
		new BasicJoinPoint(mockContext, null, null, null);
		assertTrue("BasicJoinPoint constructor should have thrown an IllegalArgumentException", false);
	}
	
	@Test
	public void testInvoke() throws Exception {
		// Run
		String result = (String) joinPoint.invoke();
		
		// Verify
		assertEquals("invoke should have returned \"invoked\"", "invoked", result);
	}
	
	@Test
	public void testHashCode_equal() {
		// Setup
		BasicJoinPoint otherJoinPoint = new BasicJoinPoint(mockContext, mockAdvisor, method, location);
		otherJoinPoint.setArguments(args);
		otherJoinPoint.setBeanName(beanName);
		otherJoinPoint.setMethod(method);
		otherJoinPoint.setTarget(target);
		
		// Run
		int firstHash = joinPoint.hashCode();
		int secondHash = otherJoinPoint.hashCode();
		
		// Verify
		assertEquals("Hash codes should be equal", firstHash, secondHash);
	}
	
	@Test
	public void testHashCode_notEqual() {
		// Setup
		BasicJoinPoint otherJoinPoint = new BasicJoinPoint(mockContext, new Object(), method, AdviceLocation.After);
		otherJoinPoint.setArguments(args);
		otherJoinPoint.setBeanName(beanName);
		otherJoinPoint.setMethod(method);
		otherJoinPoint.setTarget(target);
		
		// Run
		int firstHash = joinPoint.hashCode();
		int secondHash = otherJoinPoint.hashCode();
		
		// Verify
		assertFalse("Hash codes should not be equal", firstHash == secondHash);
	}
	
	@Test
	public void testEquals_equal() {
		// Setup
		BasicJoinPoint otherJoinPoint = new BasicJoinPoint(mockContext, mockAdvisor, method, location);
		otherJoinPoint.setArguments(args);
		otherJoinPoint.setBeanName(beanName);
		otherJoinPoint.setMethod(method);
		otherJoinPoint.setTarget(target);
		
		// Run
		boolean result = joinPoint.equals(otherJoinPoint);
		
		// Verify
		assertTrue("BasicJoinPoints should be equal", result);
	}
	
	@Test
	public void testEquals_notEqual() {
		// Setup
		BasicJoinPoint otherJoinPoint = new BasicJoinPoint(mockContext, mockAdvisor, method, AdviceLocation.After);
		otherJoinPoint.setArguments(args);
		otherJoinPoint.setBeanName(beanName);
		otherJoinPoint.setMethod(method);
		otherJoinPoint.setTarget(target);
		
		// Run
		boolean result = joinPoint.equals(otherJoinPoint);
		
		// Verify
		assertFalse("BasicJoinPoints should not be equal", result);
	}
	
	private static class MockAspect {
		
		@SuppressWarnings("unused")
		public String advice(JoinPoint joinPoint) {
			return "invoked";
		}
		
	}

}
