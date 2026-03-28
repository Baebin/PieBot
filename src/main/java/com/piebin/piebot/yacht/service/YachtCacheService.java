package com.piebin.piebot.yacht.service;

import com.piebin.piebot.yacht.domain.YachtRoom;

public interface YachtCacheService {
    boolean hasCache(String userId);
    void addCache(YachtRoom yachtRoom);
    void removeCache(YachtRoom yachtRoom);
}
