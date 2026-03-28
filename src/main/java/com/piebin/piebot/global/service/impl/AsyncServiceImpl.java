package com.piebin.piebot.global.service.impl;

import com.piebin.piebot.global.service.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncServiceImpl implements AsyncService {
    private final Map<String, CompletableFuture<?>> caches = new ConcurrentHashMap<>();

    private final Executor executor = Executors.newCachedThreadPool();

    private boolean hasAsyncCache(String key) {
        return caches.containsKey(key);
    }

    private CompletableFuture<?> getAsyncCache(String key) {
        return caches.get(key);
    }

    private void addAsyncCache(String key, CompletableFuture<?> future) {
        caches.put(key, future);
        log.info("Async Cache Added: {}", key);
    };

    private void removeAsyncCache(String key) {
        caches.remove(key);
        log.info("Async Cache Removed: {}", key);
    };

    @Override
    public boolean cancel(String key) {
        if (!hasAsyncCache(key)) {
            log.info("Async Cache Cancel Failed: {}", key);
            return false;
        };
        CompletableFuture<?> future = getAsyncCache(key);
        boolean isCanceled = false;
        if (future != null)
            isCanceled = future.cancel(true);
        log.info("Async Cache Canceled: {}, {}", key, isCanceled);
        return true;
    }

    private void initAsyncCache(String key, CompletableFuture future) {
        addAsyncCache(key, future);
        future.whenComplete((r, e) -> removeAsyncCache(key));
    }

    @Override
    public String runAsync(Runnable runnable) {
        CompletableFuture future = CompletableFuture.runAsync(runnable, executor);
        String key = System.identityHashCode(future) + "";
        initAsyncCache(key, future);
        return key;
    }

    @Override
    public void runAsync(String key, Runnable runnable) {
        CompletableFuture future = CompletableFuture.runAsync(runnable, executor);
        initAsyncCache(key, future);
    }

    @Override
    public void runAsyncWithCancel(String key, Runnable runnable) {
        if (hasAsyncCache(key))
            cancel(key);
        runAsync(key, runnable);
    }
}
