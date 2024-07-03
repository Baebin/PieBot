package com.piebin.piebot.service.impl;

import com.piebin.piebot.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    public static final String DIRECTORY = "images";

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
}
