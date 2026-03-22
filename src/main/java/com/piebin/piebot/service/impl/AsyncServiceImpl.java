package com.piebin.piebot.service.impl;

import com.piebin.piebot.service.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncServiceImpl implements AsyncService {
    private final Map<String, CompletableFuture> caches = new HashMap<>();

    private void addAsyncCache(String key, CompletableFuture future) {
        caches.put(key, future);
        log.info("Async Cache Added: {}", key);
    };

    private void removeAsyncCache(String key) {
        caches.remove(key);
        log.info("Async Cache Removed: {}", key);
    };

    @Override
    public boolean cancel(String key) {
        if (!caches.containsKey(key)) {
            log.info("Async Cache Cancel Failed: {}", key);
            return false;
        }
        caches.get(key).cancel(true);
        log.info("Async Cache Canceled: {}", key);

        removeAsyncCache(key);
        return true;
    }

    private void initAsyncCache(String key, CompletableFuture future) {
        addAsyncCache(key, future);
        future.thenRun(() -> removeAsyncCache(key));
    }

    @Override
    public String runAsync(Runnable runnable) {
        CompletableFuture future = CompletableFuture.runAsync(runnable);
        String key = System.identityHashCode(future) + "";
        initAsyncCache(key, future);
        return key;
    }

    @Override
    public void runAsync(String key, Runnable runnable) {
        CompletableFuture future = CompletableFuture.runAsync(runnable);
        initAsyncCache(key, future);
    }
}
