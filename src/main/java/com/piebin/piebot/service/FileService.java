package com.piebin.piebot.service;

import org.springframework.core.io.Resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {
    File getFile(String path, String name, String ext);
    Resource getResource(String path, String name, String ext);
    InputStream getResourceStream(String path, String name, String ext);
    BufferedImage getBufferedResourceImage(String path, String name, String ext) throws IOException;
}
