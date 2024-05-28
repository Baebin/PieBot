package com.piebin.piebot.service;

import java.time.Instant;

public interface TaskSchedulerService {
    void runAfter(Runnable runnable, Instant instant);
    void runAfterMillis(Runnable runnable, long millis);
    void runAfterSeconds(Runnable runnable, long seconds);
}
