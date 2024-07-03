package com.piebin.piebot.service;

import java.io.File;

public interface ImageService {
    File getFile(String path, String name, String ext);
}
