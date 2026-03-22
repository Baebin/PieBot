package com.piebin.piebot.service;

import com.piebin.piebot.model.domain.YachtRoom;

public interface YachtCacheService {
    boolean hasCache(String userId);
    void addCache(YachtRoom yachtRoom);
    void removeCache(YachtRoom yachtRoom);
}
