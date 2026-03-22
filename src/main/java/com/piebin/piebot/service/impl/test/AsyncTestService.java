package com.piebin.piebot.service.impl.test;

import com.piebin.piebot.service.AsyncService;
import com.piebin.piebot.service.impl.AsyncServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class AsyncTestService {
    private final AsyncService asyncService = new AsyncServiceImpl();

    @Test
    void testAsync() {
        log.info("Test");
        String key = asyncService.runAsync(() -> {
            log.info("Test A");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("Test B");
        });
        log.info("Test C");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        asyncService.cancel(key);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Test D");
    }

    @Test
    void testAsyncWithKey() {
        String key = "Piebin";
        log.info("Test");
        asyncService.runAsync(key, () -> {
            log.info("Test A");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("Test B");
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        asyncService.cancel(key);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Test D");
    }
}
