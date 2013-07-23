/*
 * Copyright (C) 2013 Clarion Media, LLC
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

import android.content.Context;

import com.clarionmedia.infinitum.aop.AdvisedProxyFactory;
import com.clarionmedia.infinitum.aop.Pointcut;
import com.clarionmedia.infinitum.di.AbstractProxy;

/**
 * <p> {@link AdvisedProxyFactory} which creates {@link AbstractProxy} instances by determining the best implementation
 * to use. {@code DelegatingAdvisedProxyFactory} will use {@link AdvisedDexMakerProxy} to proxy non-final classes and
 * {@link AdvisedJdkDynamicProxy} to proxy interfaces. </p>
 *
 * @author Tyler Treat
 * @version 1.1.0.1 07/22/13
 * @since 1.0
 */
public class DelegatingAdvisedProxyFactory implements AdvisedProxyFactory {

    @Override
    public AbstractProxy createProxy(Context context, Object object, Pointcut pointcut) {
        return createProxy(context, object, pointcut, false);
    }

    @Override
    public AbstractProxy createProxy(Context context, Object object, Pointcut pointcut, boolean bytecodeInstrumented) {
        Class<?> clazz = object.getClass();
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0 && !bytecodeInstrumented)
            return new AdvisedJdkDynamicProxy(object, pointcut, interfaces);
        return new AdvisedDexMakerProxy(context, object, pointcut);
    }

}
