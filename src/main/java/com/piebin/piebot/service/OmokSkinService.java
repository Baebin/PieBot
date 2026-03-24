package com.piebin.piebot.service;

import com.piebin.piebot.model.domain.OmokRoom;
import com.piebin.piebot.model.entity.OmokState;

import java.io.File;
import java.util.function.Consumer;

public interface OmokSkinService {
    File getBoard(OmokRoom omokRoom);

    void updateBoard(OmokRoom omokRoom, OmokState omokState, char x, int y, Consumer<File> consumer);
}
