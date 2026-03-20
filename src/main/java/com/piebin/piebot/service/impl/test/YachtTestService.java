package com.piebin.piebot.service.impl.test;

import com.piebin.piebot.factory.YachtLocationFactory;
import com.piebin.piebot.factory.impl.YachtLocationFactoryImpl;
import com.piebin.piebot.service.ImageService;
import com.piebin.piebot.service.impl.ImageServiceImpl;
import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
public class YachtTestService {
    private static final String BOARD = "board";

    private final ImageService imageService = new ImageServiceImpl();
    private final YachtLocationFactory yachtLocationFactory = new YachtLocationFactoryImpl();

    private File getBoard(String boardName) {
        File file = imageService.getFile("yacht", boardName, "png");
        if (!file.exists()) {
            ClassPathResource resource = new ClassPathResource("yacht/" + BOARD + ".png");
            try {
                return resource.getFile();
            } catch (IOException e) {}
        }
        return file;
    }

    @Test
    public void testGetBoard() {
        log.info(getBoard(BOARD).getAbsolutePath());
    }

    @Test
    public void testRandomDiceLocation() {
        int i;
        for (i = 1; i <= 5; i++)
            log.info(
                    "{}. Pre. {} -> After. {}", i,
                    yachtLocationFactory.getNonSelectedDiceLocations().get(i - 1),
                    yachtLocationFactory.getRandomDiceLocation(i)
            );

        i = 0;
        for (Object o : yachtLocationFactory.getRandomDiceLocations())
            log.info("{}. After. {}", ++i, o);
    }

    private void drawImage(Graphics2D graphics2D, BufferedImage bufferedImage, Pair<Integer, Integer> loc) {
        graphics2D.drawImage(
                bufferedImage,
                loc.component1(), loc.component2(), null);
    }

    private void drawString(Graphics2D graphics2D, String text, Pair<Integer, Integer> loc, boolean hasWeight) {
        graphics2D.drawString(text, loc.component1() + (hasWeight ? yachtLocationFactory.getSectionXWeight() : 0), loc.component2());
    }

    @Test
    public void testMakeFile() throws IOException, FontFormatException {
        File file = imageService.getFile("yacht", "board", "png");
        log.info(file.getAbsolutePath());

        BufferedImage bufferedImageBoard = imageService.getBufferedResourceImage("yacht", "board", "png");

        /*
        Dice
        */

        List<BufferedImage> bufferedDiceImages = new ArrayList<>();
        for (String name : Arrays.asList("one", "two", "three", "four", "five", "six"))
            bufferedDiceImages.add(imageService.getBufferedResourceImage("yacht", "dice_" + name, "png"));

        List<Pair<Integer, Integer>> diceLocations = yachtLocationFactory.getSelectedDiceLocations();

        Graphics2D graphics2D = bufferedImageBoard.createGraphics();
        for (int i = 0; i < 5; i++)
            drawImage(graphics2D, bufferedDiceImages.get(i), diceLocations.get(i));
        for (int i = 1; i <= 5; i++)
            drawImage(graphics2D, bufferedDiceImages.get(new Random().nextInt(6)), yachtLocationFactory.getRandomDiceLocation(i));

        /*
        Player Board
        */

        // Font Init
        FileInputStream fileInputStream = new FileInputStream(imageService.getResourceFile("fonts", "Bazzi", "ttf"));
        Font font = Font.createFont(Font.TRUETYPE_FONT, fileInputStream).deriveFont(Font.BOLD, 30f);
        graphics2D.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );
        graphics2D.setFont(font);
        graphics2D.setColor(Color.BLACK);

        // Point
        boolean hasWeight = false;
        for (int i = 0; i < 2; i++) {
            hasWeight = !hasWeight;
            for (int j = 0; j < 6; j++)
                drawString(graphics2D, "30", yachtLocationFactory.getNumberLocation(j + 1), hasWeight);
            drawString(graphics2D, "30", yachtLocationFactory.getBonusLocation(), hasWeight);
            drawString(graphics2D, "30", yachtLocationFactory.getChoiceLocation(), hasWeight);
            drawString(graphics2D, "30", yachtLocationFactory.getFourOfAKindLocation(), hasWeight);
            drawString(graphics2D, "30", yachtLocationFactory.getFullHouseLocation(), hasWeight);
            drawString(graphics2D, "30", yachtLocationFactory.getSmallStraightLocation(), hasWeight);
            drawString(graphics2D, "30", yachtLocationFactory.getLargeStraightHouseLocation(), hasWeight);
            drawString(graphics2D, "30", yachtLocationFactory.getYachtLocation(), hasWeight);
            drawString(graphics2D, "30", yachtLocationFactory.getTotalPointsLocation(), hasWeight);
        }

        // Make File
        file.mkdirs();
        ImageIO.write(bufferedImageBoard, "png", file);
    }
}
