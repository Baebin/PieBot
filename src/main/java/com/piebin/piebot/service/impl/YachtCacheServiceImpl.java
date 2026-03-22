package com.piebin.piebot.service.impl;

import com.piebin.piebot.model.domain.YachtRoom;
import com.piebin.piebot.model.repository.YachtRoomRepository;
import com.piebin.piebot.service.YachtCacheService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class YachtCacheServiceImpl implements YachtCacheService {
    // UserId, YachtRoomIdx
    private final Map<String, Long> caches = new HashMap<>();

    private final YachtRoomRepository yachtRoomRepository;

    @PostConstruct
    private void initCaches() {
        List<YachtRoom> yachtRooms = yachtRoomRepository.findAll();
        for (YachtRoom yachtRoom : yachtRooms)
            addCache(yachtRoom);
    }

    @Override
    public boolean hasCache(String userId) {
        return caches.containsKey(userId);
    }

    @Override
    public void addCache(YachtRoom yachtRoom) {
        caches.put(yachtRoom.getAccount().getId(), yachtRoom.getIdx());
        caches.put(yachtRoom.getOpponent().getId(), yachtRoom.getIdx());
        log.info("Yacht Room Cache Added: {}", yachtRoom.getIdx());
    }

    @Override
    public void removeCache(YachtRoom yachtRoom) {
        caches.remove(yachtRoom.getAccount().getId());
        caches.remove(yachtRoom.getOpponent().getId());
        log.info("Yacht Room Cache Removed: {}", yachtRoom.getIdx());
    }
}
