package com.piebin.piebot.global.service.impl;

import com.piebin.piebot.global.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    public static final String DIRECTORY = "images";

    private final ResourceLoader resourceLoader;

    private String getPath() {
        return new File("").getAbsolutePath() + "/";
    }

    private String getFilePath(String path, String name, String ext) {
        return (getPath()
                + DIRECTORY + "/"
                + path + "/"
                + name + "." + ext)
                .replace("\\", "/");
    }

    @Override
    public File getFile(String path, String name, String ext) {
        return new File(getFilePath(path, name, ext));
    }

    @Override
    public Resource getResource(String path, String name, String ext) {
        try {
            log.info("Thread: " + Thread.currentThread().getName());
            log.info("Class: " + getClass().getName());
            Resource r = resourceLoader.getResource("classpath:" + path + "/" + name + "." + ext);
            log.info("Resource: " + r);
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getResourceStream(String path, String name, String ext) {
        try {
            return getClass()
                    .getClassLoader()
                    .getResourceAsStream(path + "/" + name + "." + ext);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BufferedImage getBufferedResourceImage(String path, String name, String ext) {
        try {
            ImageIO.setUseCache(false);
            return ImageIO.read(getResourceStream(path, name, ext));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
