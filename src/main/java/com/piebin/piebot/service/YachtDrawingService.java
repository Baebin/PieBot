package com.piebin.piebot.service;

import com.piebin.piebot.model.domain.YachtRoom;

import java.io.File;
import java.io.IOException;

public interface YachtDrawingService {
    File getBoard(YachtRoom yachtRoom, boolean isNewFile) throws IOException;
}
