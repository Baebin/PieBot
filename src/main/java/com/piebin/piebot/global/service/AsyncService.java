package com.piebin.piebot.global.service;

public interface AsyncService {
    boolean cancel(String key);
    String runAsync(Runnable runnable);
    void runAsync(String key, Runnable runnable);
    void runAsyncWithCancel(String key, Runnable runnable);
}
