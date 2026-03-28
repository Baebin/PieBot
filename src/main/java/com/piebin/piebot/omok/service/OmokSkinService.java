package com.piebin.piebot.omok.service;

import com.piebin.piebot.omok.domain.OmokRoom;
import com.piebin.piebot.omok.entity.OmokState;

import java.io.File;
import java.util.function.Consumer;

public interface OmokSkinService {
    File getBoard(OmokRoom omokRoom);

    void updateBoard(OmokRoom omokRoom, OmokState omokState, char x, int y, Consumer<File> consumer);
}
