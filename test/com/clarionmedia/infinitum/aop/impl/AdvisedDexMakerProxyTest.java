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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import java.lang.reflect.Method;
import java.util.PriorityQueue;
import java.util.Queue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.clarionmedia.infinitum.aop.JoinPoint;
import com.clarionmedia.infinitum.aop.JoinPoint.AdviceLocation;
import com.clarionmedia.infinitum.aop.Pointcut;
import com.clarionmedia.infinitum.aop.ProceedingJoinPoint;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class AdvisedDexMakerProxyTest {

	private AdvisedDexMakerProxy proxy;
	private Pointcut mockPointcut;
	private BasicJoinPoint mockJoinPoint;
	private ProceedingJoinPoint mockProceedingJoinPoint;
	private Integer target;

	@Before
	public void setup() {
		mockPointcut = mock(Pointcut.class);
		mockJoinPoint = mock(BasicJoinPoint.class);
		mockProceedingJoinPoint = mock(ProceedingJoinPoint.class);
		target = new Integer(42);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullPointcutThrowsException() throws Throwable {
		
		// Setup
		mockPointcut = null;
		proxy = new AdvisedDexMakerProxy(Robolectric.application, target, mockPointcut);
		
		// Verify
		assertTrue("Proxy constructor should have thrown an IllegalArgumentException", false);
		
	}

	@Test
	public void testInvoke_noAdvice() throws Throwable {

		// Setup
		Method method = target.getClass().getMethod("toString");
		when(mockPointcut.getJoinPoints()).thenReturn(new PriorityQueue<JoinPoint>());
		proxy = new AdvisedDexMakerProxy(Robolectric.application, target, mockPointcut);

		// Run
		Object result = proxy.invoke(proxy, method, new Object[0]);

		// Verify
		assertNotNull("Proxy should have returned target value", result);
		assertThat("Proxy should have returned target value", result, is(String.class));
		assertTrue("Proxy should have returned target value", result.equals("42"));

	}
	
	@Test
	public void testInvoke_nonApplicableAdvice() throws Throwable {

		// Setup
		Method method = target.getClass().getMethod("toString");
		Queue<JoinPoint> advice = new PriorityQueue<JoinPoint>();
		advice.add(mockJoinPoint);
		when(mockJoinPoint.getLocation()).thenReturn(AdviceLocation.Before);
		when(mockJoinPoint.isClassScope()).thenReturn(false);
		when(mockJoinPoint.getMethod()).thenReturn(null);
		when(mockPointcut.getJoinPoints()).thenReturn(advice);
		proxy = new AdvisedDexMakerProxy(Robolectric.application, target, mockPointcut);

		// Run
		Object result = proxy.invoke(proxy, method, new Object[0]);

		// Verify
		verify(mockJoinPoint, times(0)).invoke();
		assertNotNull("Proxy should have returned target value", result);
		assertThat("Proxy should have returned target value", result, is(String.class));
		assertTrue("Proxy should have returned target value", result.equals("42"));
	}


	@Test
	public void testInvoke_beforeAdvice() throws Throwable {

		// Setup
		Method method = target.getClass().getMethod("toString");
		Queue<JoinPoint> advice = new PriorityQueue<JoinPoint>();
		advice.add(mockJoinPoint);
		when(mockJoinPoint.getLocation()).thenReturn(AdviceLocation.Before);
		when(mockJoinPoint.isClassScope()).thenReturn(true);
		when(mockPointcut.getJoinPoints()).thenReturn(advice);
		proxy = new AdvisedDexMakerProxy(Robolectric.application, target, mockPointcut);

		// Run
		Object result = proxy.invoke(proxy, method, new Object[0]);

		// Verify
		verify(mockJoinPoint).invoke();
		assertNotNull("Proxy should have returned target value", result);
		assertThat("Proxy should have returned target value", result, is(String.class));
		assertTrue("Proxy should have returned target value", result.equals("42"));

	}

	@Test
	public void testInvoke_afterAdvice() throws Throwable {

		// Setup
		Method method = target.getClass().getMethod("toString");
		Queue<JoinPoint> advice = new PriorityQueue<JoinPoint>();
		advice.add(mockJoinPoint);
		when(mockJoinPoint.getLocation()).thenReturn(AdviceLocation.After);
		when(mockJoinPoint.isClassScope()).thenReturn(true);
		when(mockPointcut.getJoinPoints()).thenReturn(advice);
		proxy = new AdvisedDexMakerProxy(Robolectric.application, target, mockPointcut);

		// Run
		Object result = proxy.invoke(proxy, method, new Object[0]);

		// Verify
		verify(mockJoinPoint).invoke();
		assertNotNull("Proxy should have returned target value", result);
		assertThat("Proxy should have returned target value", result, is(String.class));
		assertTrue("Proxy should have returned target value", result.equals("42"));

	}

	@Test
	public void testInvoke_aroundAdvice() throws Throwable {

		// Setup
		Method method = target.getClass().getMethod("toString");
		Queue<JoinPoint> advice = new PriorityQueue<JoinPoint>();
		advice.add(mockProceedingJoinPoint);
		when(mockProceedingJoinPoint.getLocation()).thenReturn(AdviceLocation.Around);
		when(mockProceedingJoinPoint.isClassScope()).thenReturn(true);
		when(mockProceedingJoinPoint.invoke()).thenReturn(method.invoke(target, new Object[0]));
		when(mockPointcut.getJoinPoints()).thenReturn(advice);
		proxy = new AdvisedDexMakerProxy(Robolectric.application, target, mockPointcut);

		// Run
		Object result = proxy.invoke(proxy, method, new Object[0]);

		// Verify
		verify(mockProceedingJoinPoint).invoke();
		assertNotNull("Proxy should have returned target value", result);
		assertThat("Proxy should have returned target value", result, is(String.class));
		assertTrue("Proxy should have returned target value", result.equals("42"));

	}

	@Test
	public void testInvoke_aroundAdviceIntercept() throws Throwable {

		// Setup
		Method method = target.getClass().getMethod("toString");
		Queue<JoinPoint> advice = new PriorityQueue<JoinPoint>();
		advice.add(mockProceedingJoinPoint);
		when(mockProceedingJoinPoint.getLocation()).thenReturn(AdviceLocation.Around);
		when(mockProceedingJoinPoint.isClassScope()).thenReturn(true);
		when(mockProceedingJoinPoint.invoke()).thenReturn(null);
		when(mockPointcut.getJoinPoints()).thenReturn(advice);
		proxy = new AdvisedDexMakerProxy(Robolectric.application, target, mockPointcut);

		// Run
		Object result = proxy.invoke(proxy, method, new Object[0]);

		// Verify
		verify(mockProceedingJoinPoint).invoke();
		assertNull("Proxy should have returned null", result);

	}
}
