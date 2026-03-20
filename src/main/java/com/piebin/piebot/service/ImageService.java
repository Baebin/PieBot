package com.piebin.piebot.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface ImageService {
    File getFile(String path, String name, String ext);
    File getResourceFile(String path, String name, String ext);
    BufferedImage getBufferedResourceImage(String path, String name, String ext) throws IOException;
}
