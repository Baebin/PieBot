package com.piebin.piebot.service.impl;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.YachtRoom;
import com.piebin.piebot.model.repository.YachtRoomRepository;
import com.piebin.piebot.service.ImageService;
import com.piebin.piebot.service.YachtService;
import com.piebin.piebot.utility.MessageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YachtServiceImpl implements YachtService {
    private static final String BOARD = "board";

    private final ImageService imageService;

    @Override
    public File getBoard(YachtRoom yachtRoom) {
        File file = imageService.getFile("omok", yachtRoom.getIdx() + "", "png");
        if (!file.exists()) {
            ClassPathResource resource = new ClassPathResource("yacht/" + BOARD + ".png");
            try {
                return resource.getFile();
            } catch (IOException e) {
            }
        }
        return file;
    }

    @Override
    public String getBoardString(YachtRoom yachtRoom) {
        List<String> lines = new ArrayList<>();
        lines.add(
                "## " + MessageManager.getMention(yachtRoom.getAccount().getId())
                        + " vs " + MessageManager.getMention(yachtRoom.getOpponent().getId())
        );
        lines.add("> 현재 차례: " + MessageManager.getMention((yachtRoom.getTurnCount() == 0 ? yachtRoom.getAccount() : yachtRoom.getOpponent()).getId()));
        lines.add("> *ex) z 1, z 3, z 5, z 포커, z 풀하우스, etc.");

        String board = String.join("\n", lines);
        return board;
    }
}
