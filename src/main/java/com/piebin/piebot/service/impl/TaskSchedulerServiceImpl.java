package com.piebin.piebot.service.impl;

import com.piebin.piebot.service.TaskSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TaskSchedulerServiceImpl implements TaskSchedulerService {
    private final TaskScheduler taskScheduler;

    @Override
    public void runAfter(Runnable runnable, Instant instant) {
        taskScheduler.schedule(runnable, instant);
    }

    @Override
    public void runAfterMillis(Runnable runnable, long millis) {
        Instant instant = new Date().toInstant().plusMillis(millis);
        runAfter(runnable, instant);
    }

    @Override
    public void runAfterSeconds(Runnable runnable, long seconds) {
        Instant instant = new Date().toInstant().plusSeconds(seconds);
        runAfter(runnable, instant);
    }
}
