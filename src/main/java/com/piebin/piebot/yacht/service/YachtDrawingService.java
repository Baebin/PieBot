package com.piebin.piebot.yacht.service;

import com.piebin.piebot.yacht.domain.YachtRoom;

import java.io.IOException;
import java.io.InputStream;

public interface YachtDrawingService {
    InputStream getBoard(YachtRoom yachtRoom, boolean isNewFile) throws IOException;
}
