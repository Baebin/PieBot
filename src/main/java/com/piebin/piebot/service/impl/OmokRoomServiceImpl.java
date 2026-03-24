package com.piebin.piebot.service.impl;

import com.piebin.piebot.model.domain.OmokRoom;
import com.piebin.piebot.model.repository.OmokRoomRepository;
import com.piebin.piebot.service.OmokRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OmokRoomServiceImpl implements OmokRoomService {
    private final OmokRoomRepository omokRoomRepository;

    @Override
    @Transactional
    public void updateMessageId(long roomIdx, String messageId) {
        Optional<OmokRoom> optionalOmokRoom = omokRoomRepository.findByIdx(roomIdx);
        if (optionalOmokRoom.isEmpty())
            return;
        OmokRoom omokRoom = optionalOmokRoom.get();
        omokRoom.setMessageId(messageId);
    }
}
