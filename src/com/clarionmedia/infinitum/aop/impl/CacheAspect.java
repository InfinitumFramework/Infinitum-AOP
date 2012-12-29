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

public class CacheAspect {

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
