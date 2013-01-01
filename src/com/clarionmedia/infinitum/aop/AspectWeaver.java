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

import java.util.Set;

import android.content.Context;

/**
 * <p>
 * Responsible for generating advice-woven bytecode.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/12/12
 * @since 1.0
 */
public interface AspectWeaver {

	/**
	 * Weaves the advice from the given aspects into the framework-managed
	 * components and generates bytecode for them.
	 * 
	 * @param context
	 *            the {@link Context} used to retrieve the bytecode cache from
	 * @param aspects
	 *            the aspects to weave
	 */
	void weave(Context context, Set<AspectDefinition> aspects);

}
