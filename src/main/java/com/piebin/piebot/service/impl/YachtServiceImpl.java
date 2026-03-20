package com.piebin.piebot.service.impl;

import com.piebin.piebot.model.domain.YachtRoom;
import com.piebin.piebot.model.repository.YachtRepository;
import com.piebin.piebot.service.ImageService;
import com.piebin.piebot.service.YachtService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

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
}
