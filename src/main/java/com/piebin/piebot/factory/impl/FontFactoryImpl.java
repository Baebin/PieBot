package com.piebin.piebot.factory.impl;

import com.piebin.piebot.factory.FontFactory;
import com.piebin.piebot.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class FontFactoryImpl implements FontFactory {
    private static final String FILE_PATH = "fonts";
    private static final String FILE_EXT = "ttf";

    private static final String FONT_BAZZI = "Bazzi";

    private final ImageService imageService;

    @Override
    public Font getFont(String name, int style, float size) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(imageService.getResourceFile(FILE_PATH, name, FILE_EXT));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            return Font.createFont(Font.TRUETYPE_FONT, fileInputStream).deriveFont(style, size);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Font getBazziFont(int style, float size) {
        return getFont(FONT_BAZZI, style, size);
    }

    @Bean
    @Override
    public Font getBazziFont30fBold() {
        return getBazziFont(Font.BOLD, 30);
    }
}
