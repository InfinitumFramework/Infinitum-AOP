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

import com.clarionmedia.infinitum.aop.JoinPoint;
import com.clarionmedia.infinitum.aop.context.InfinitumAopContext;
import com.clarionmedia.infinitum.event.AbstractEvent;
import com.clarionmedia.infinitum.event.annotation.Event;
import com.clarionmedia.infinitum.event.annotation.EventPayload;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class EventsAspectTest {

    private static final String EVENT_NAME = "barEvent";

    @Mock
    private JoinPoint mockJoinPoint;

    @Mock
    private InfinitumAopContext mockAopContext;

    private EventsAspect eventsAspect;
    private Method methodUnnamedEvent;
    private Method methodNamedEvent;

    @Before
    public void setup() throws NoSuchMethodException {
        MockitoAnnotations.initMocks(this);
        eventsAspect = new EventsAspect();
        methodUnnamedEvent = Foo.class.getMethod("bar", int.class, String.class);
        methodNamedEvent = Foo.class.getMethod("barNamed", int.class, String.class);
        when(mockJoinPoint.getContext()).thenReturn(mockAopContext);
    }

    @Test
    public void testPublishEvent_unnamed() {
        // Setup
        when(mockJoinPoint.getMethod()).thenReturn(methodUnnamedEvent);
        Foo foo = new Foo();
        when(mockJoinPoint.getTarget()).thenReturn(foo);
        Object[] args = new Object[]{42, "hello, world"};
        when(mockJoinPoint.getArguments()).thenReturn(args);

        // Run
        eventsAspect.publishEvent(mockJoinPoint);

        // Verify
        ArgumentCaptor<AbstractEvent> eventCaptor = ArgumentCaptor.forClass(AbstractEvent.class);
        verify(mockAopContext).publishEvent(eventCaptor.capture());
        assertEquals("Event name should equal the method name", methodUnnamedEvent.getName(), eventCaptor.getValue().getName());
        assertEquals("Event publisher should equal the expected publisher", foo, eventCaptor.getValue().getPublisher());
        assertEquals("Event payload value should equal the expected value",
                args[0], eventCaptor.getValue().getPayloadValue("xVal"));
        assertEquals("Event payload value should equal the expected value",
                args[1], eventCaptor.getValue().getPayloadValue("yVal"));
    }

    @Test
    public void testPublishEvent_named() {
        // Setup
        when(mockJoinPoint.getMethod()).thenReturn(methodNamedEvent);
        Foo foo = new Foo();
        when(mockJoinPoint.getTarget()).thenReturn(foo);
        Object[] args = new Object[]{42, "hello, world"};
        when(mockJoinPoint.getArguments()).thenReturn(args);

        // Run
        eventsAspect.publishEvent(mockJoinPoint);

        // Verify
        ArgumentCaptor<AbstractEvent> eventCaptor = ArgumentCaptor.forClass(AbstractEvent.class);
        verify(mockAopContext).publishEvent(eventCaptor.capture());
        assertEquals("Event name should equal the expected name", EVENT_NAME, eventCaptor.getValue().getName());
        assertEquals("Event publisher should equal the expected publisher", foo, eventCaptor.getValue().getPublisher());
        assertEquals("Event payload value should equal the expected value",
                args[0], eventCaptor.getValue().getPayloadValue("xVal"));
        assertEquals("Event payload value should equal the expected value",
                args[1], eventCaptor.getValue().getPayloadValue("yVal"));
    }

    private class Foo {

        @Event
        public void bar(@EventPayload("xVal") int x, @EventPayload("yVal") String y) {

        }

        @Event(EVENT_NAME)
        public void barNamed(@EventPayload("xVal") int x, @EventPayload("yVal") String y) {

        }

    }

}
