package com.piebin.piebot.service.impl;

import com.piebin.piebot.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

    @Override
    public File getResourceFile(String path, String name, String ext) {
        try {
            return new ClassPathResource(path + "/" + name + "." + ext).getFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BufferedImage getBufferedResourceImage(String path, String name, String ext) {
        try {
            return ImageIO.read(getResourceFile(path, name, ext));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
