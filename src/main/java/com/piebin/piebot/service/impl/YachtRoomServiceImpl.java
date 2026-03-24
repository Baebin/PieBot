package com.piebin.piebot.service.impl;

import com.piebin.piebot.model.domain.YachtRoom;
import com.piebin.piebot.model.repository.YachtRoomRepository;
import com.piebin.piebot.service.YachtRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YachtRoomServiceImpl implements YachtRoomService {
    private final YachtRoomRepository YachtRoomRepository;

    @Override
    @Transactional
    public void updateMessageInfo(long roomIdx, String channelId, String messageId) {
        Optional<YachtRoom> optionalYachtRoom = YachtRoomRepository.findByIdx(roomIdx);
        if (optionalYachtRoom.isEmpty())
            return;
        YachtRoom yachtRoom = optionalYachtRoom.get();
        yachtRoom.setChannelId(channelId);
        yachtRoom.setMessageId(messageId);
    }
}
