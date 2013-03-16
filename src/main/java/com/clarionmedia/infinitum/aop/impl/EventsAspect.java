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
import com.clarionmedia.infinitum.aop.annotation.After;
import com.clarionmedia.infinitum.event.annotation.Event;
import com.clarionmedia.infinitum.event.annotation.EventPayload;
import com.clarionmedia.infinitum.event.impl.FrameworkEvent;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Aspect containing advice used for the event framework.
 * </p>
 *
 * @author Tyler Treat
 * @version 1.0.4 03/13/13
 * @since 1.0.4
 */
public class EventsAspect {

    /**
     * Publishes a framework event if the method is annotated with {@link Event} after it's invoked.
     */
    @After
    public void publishEvent(JoinPoint joinPoint) {
        Event anno = joinPoint.getMethod().getAnnotation(Event.class);
        if (anno == null)
            return;
        String name = anno.value();
        if (name.equals(""))
            name = joinPoint.getMethod().getName();

        // Build the event payload
        Map<String, Object> payload = new HashMap<String, Object>();
        Annotation[][] annos = joinPoint.getMethod().getParameterAnnotations();
        Object[] args = joinPoint.getArguments();
        for (int i = 0; i < annos.length; i++) {
            for (Annotation a : annos[i]) {
                if (a.annotationType() == EventPayload.class) {
                    EventPayload eventPayload = (EventPayload) a;
                    payload.put(eventPayload.value(), args[i]);
                }
            }
        }

        // Publish the event
        joinPoint.getContext().publishEvent(new FrameworkEvent(name, joinPoint.getTarget(), payload));
    }

}
