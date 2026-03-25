package com.piebin.piebot.service;

import com.piebin.piebot.model.domain.YachtRoom;

import java.io.IOException;
import java.io.InputStream;

public interface YachtDrawingService {
    InputStream getBoard(YachtRoom yachtRoom, boolean isNewFile) throws IOException;
}
