package com.piebin.piebot.service;

public interface AsyncService {
    boolean cancel(String key);
    String runAsync(Runnable runnable);
    void runAsync(String key, Runnable runnable);
}
