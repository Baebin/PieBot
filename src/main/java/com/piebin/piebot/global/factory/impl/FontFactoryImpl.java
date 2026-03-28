package com.piebin.piebot.global.factory.impl;

import com.piebin.piebot.global.factory.FontFactory;
import com.piebin.piebot.global.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
public class FontFactoryImpl implements FontFactory {
    private static final String FILE_PATH = "fonts";
    private static final String FILE_EXT = "ttf";

    private static final String FONT_BAZZI = "Bazzi";

    private final FileService fileService;

    @Override
    public Font getFont(String name, int style, float size) {
        InputStream inputStream = fileService.getResourceStream(FILE_PATH, name, FILE_EXT);

        try {
            return Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(style, size);
        } catch (FontFormatException e) {
            // throw new RuntimeException(e);
        } catch (IOException e) {
            // throw new RuntimeException(e);
        }
        return new Font("Arial", Font.PLAIN, (int) size);
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

    @Override
    public Font getArialFont(int style, float size) {
        return new Font("Arial", style, (int) size);
    }

    @Bean
    @Override
    public Font getArialFont30f() {
        return getArialFont(Font.PLAIN, 30);
    }

    @Bean
    @Override
    public Font getArialFont30fBold() {
        return getArialFont(Font.BOLD, 30);
    }
}
