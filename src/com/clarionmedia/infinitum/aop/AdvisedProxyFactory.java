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

package com.clarionmedia.infinitum.aop;

import android.content.Context;

import com.clarionmedia.infinitum.di.AbstractProxy;

/**
 * <p>
 * Factory for creating advice-woven {@link AbstractProxy} instances. This
 * factory will use the most appropriate {@code AbstractProxy} implementation
 * for the {@link Object} being proxied. If possible, a {@link JdkDynamicProxy}
 * will be used to avoid bytecode instrumentation. Otherwise, a
 * {@link DexMakerProxy} will be used.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/14/12
 * @since 1.0
 */
public interface AdvisedProxyFactory {

	/**
	 * Creates a new {@link AbstractProxy} for the given {@link Object}.
	 * 
	 * @param context
	 *            the {@link Context} used to retrieve the DEX bytecode cache if
	 *            it's needed
	 * @param object
	 *            the {@code Object} to proxy
	 * @param pointcut
	 *            the {@link Pointcut} containing advice
	 * @return {@code AbstractProxy}
	 */
	AbstractProxy createProxy(Context context, Object object, Pointcut pointcut);

}
