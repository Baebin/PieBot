package com.piebin.piebot.service;

import com.piebin.piebot.model.domain.YachtRoom;

import java.io.File;

public interface YachtService {
    File getBoard(YachtRoom yachtRoom);
    String getBoardString(YachtRoom yachtRoom);
}
