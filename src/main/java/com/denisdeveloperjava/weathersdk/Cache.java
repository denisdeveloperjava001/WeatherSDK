package com.denisdeveloperjava.weathersdk;

import com.denisdeveloperjava.weathersdk.model.CachedResponse;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

class Cache {

    private static ScheduledExecutorService cacheUpdateExecutor;
    private static final Map<String, CachedResponse> cacheCityWeather = new LinkedHashMap<>();

    private static final Set<String> usedApiKeys = new HashSet<>();

    protected static Set<String> getUsedApiKeys() {
        return usedApiKeys;
    }

    protected static Map<String, CachedResponse> getCacheCityWeather() {
        return cacheCityWeather;
    }

    protected static ScheduledExecutorService getCacheUpdateExecutor() {
        return cacheUpdateExecutor;
    }

    protected static void setCacheUpdateExecutor(ScheduledExecutorService executorService) {
        Cache.cacheUpdateExecutor = executorService;
    }
}
