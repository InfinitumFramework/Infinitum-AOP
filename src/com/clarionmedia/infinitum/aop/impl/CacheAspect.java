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
import java.util.Map;

import com.clarionmedia.infinitum.aop.JoinPoint;
import com.clarionmedia.infinitum.aop.ProceedingJoinPoint;
import com.clarionmedia.infinitum.aop.annotation.Around;
import com.clarionmedia.infinitum.aop.annotation.Before;
import com.clarionmedia.infinitum.aop.annotation.Cache;
import com.clarionmedia.infinitum.aop.annotation.EvictCache;
import com.clarionmedia.infinitum.internal.ObjectUtils;
import com.clarionmedia.infinitum.internal.caching.LruCache;

/**
 * <p>
 * Aspect containing advice used for cache abstraction.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/28/12
 * @since 1.0
 */
public class CacheAspect {

	/**
	 * Retrieves the cached result if available or otherwise invokes the method
	 * and caches the result.
	 */
	@Around
	private Object cache(ProceedingJoinPoint joinPoint) throws Exception {
		Cache anno = joinPoint.getMethod().getAnnotation(Cache.class);
		if (anno == null)
			return joinPoint.proceed();
		Map<String, Map<Integer, Object>> methodCache = joinPoint.getContext().getMethodCache();
		String cacheName = anno.value();
		Map<Integer, Object> cache = methodCache.get(cacheName);
		if (cache == null) {
			cache = new LruCache<Integer, Object>(15);
			methodCache.put(cacheName, cache);
		}
		int key = computeHash(joinPoint);
		if (cache.containsKey(key))
			return cache.get(key);
		Object result = joinPoint.proceed();
		cache.put(key, result);
		return result;
	}

	/**
	 * Evicts the specified cache(s).
	 */
	@Before
	private void evictCache(JoinPoint joinPoint) {
		EvictCache anno = joinPoint.getMethod().getAnnotation(EvictCache.class);
		if (anno == null)
			return;
		Map<String, Map<Integer, Object>> methodCache = joinPoint.getContext().getMethodCache();
		String[] cacheNames = anno.value();
		if (cacheNames[0].equals("") && cacheNames.length == 1) {
			methodCache.clear();
		} else {
			for (String cacheName : cacheNames) {
				Map<Integer, Object> cache = methodCache.get(cacheName);
				if (cache != null)
					cache.clear();
			}
		}
	}

	private int computeHash(JoinPoint joinPoint) {
		Method method = joinPoint.getMethod();
		final int PRIME = 7;
		int hash = 31;
		hash = PRIME * hash + method.getDeclaringClass().getName().hashCode();
		hash = PRIME * hash + method.getName().hashCode();
		hash = PRIME * hash + ObjectUtils.nullSafeHashCode(joinPoint.getArguments());
		return hash;
	}

}
