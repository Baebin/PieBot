package com.piebin.piebot.yacht.service.impl;

import com.piebin.piebot.global.factory.FontFactory;
import com.piebin.piebot.yacht.factory.YachtLocationFactory;
import com.piebin.piebot.yacht.domain.YachtRoom;
import com.piebin.piebot.global.service.FileService;
import com.piebin.piebot.yacht.service.YachtDrawingService;
import com.piebin.piebot.yacht.domain.YachtScoreBoard;
import kotlin.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YachtDrawingServiceImpl implements YachtDrawingService {
    private static final int FILE_WIDTH = (int) (1536 * 0.5);
    private static final int FILE_HEIGHT = (int) (1024 * 0.5);

    private static final String FILE_PATH = "yacht";
    private static final String FILE_EXT = "png";

    private static final String FILE_BOARD = "board";
    private static final String FILE_DICES_PREFIX = "dice_";
    private static final List<String> FILE_DICES_NAMES = Arrays.asList("one", "two", "three", "four", "five", "six");

    private final FontFactory fontFactory;
    private final YachtLocationFactory yachtLocationFactory;

    private final FileService fileService;

    /*
    File Setting
    */

    private BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, image.getType());

        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }

    /*
    Drawing
    */

    private void drawImage(Graphics2D graphics2D, BufferedImage bufferedImage, Pair<Integer, Integer> loc) {
        graphics2D.drawImage(
                bufferedImage,
                loc.component1(), loc.component2(), null);
    }

    private void drawNumber(Graphics2D graphics2D, Integer number, Pair<Integer, Integer> loc, boolean isOpponent) {
        if (number == null)
            return;
        drawString(graphics2D, number + "", loc, isOpponent);
    }

    private void drawString(Graphics2D graphics2D, String text, Pair<Integer, Integer> loc, boolean isOpponent) {
        int weight = 5 - ((text.length() - 1) * 9);
        graphics2D.drawString(
                text,
                loc.component1()
                        + (isOpponent ? yachtLocationFactory.getSectionXWeight() : 0)
                        + weight,
                loc.component2());
    }

    /*
    Score Board
    */

    private void drawNumberScores(Graphics2D graphics2D, YachtScoreBoard scoreBoard, boolean isOpponent) {
        List<Integer> scores = scoreBoard.getNumberScores();
        for (int i = 0; i < 6; i++)
            drawNumber(graphics2D, scores.get(i), yachtLocationFactory.getNumberLocation(i + 1), isOpponent);
    }

    private void drawBonusScore(Graphics2D graphics2D, YachtScoreBoard scoreBoard, boolean isOpponent) {
        drawNumber(graphics2D, scoreBoard.getBonus(), yachtLocationFactory.getBonusLocation(), isOpponent);
    }

    private void drawChoiceScore(Graphics2D graphics2D, YachtScoreBoard scoreBoard, boolean isOpponent) {
        drawNumber(graphics2D, scoreBoard.getChoice(), yachtLocationFactory.getChoiceLocation(), isOpponent);
    }

    private void drawFourOfAKindScore(Graphics2D graphics2D, YachtScoreBoard scoreBoard, boolean isOpponent) {
        drawNumber(graphics2D, scoreBoard.getFourOfAKind(), yachtLocationFactory.getFourOfAKindLocation(), isOpponent);
    }

    private void drawFullHouseScore(Graphics2D graphics2D, YachtScoreBoard scoreBoard, boolean isOpponent) {
        drawNumber(graphics2D, scoreBoard.getFullHouse(), yachtLocationFactory.getFullHouseLocation(), isOpponent);
    }

    private void drawSmallStraightScore(Graphics2D graphics2D, YachtScoreBoard scoreBoard, boolean isOpponent) {
        drawNumber(graphics2D, scoreBoard.getSmallStraight(), yachtLocationFactory.getSmallStraightLocation(), isOpponent);
    }

    private void drawLargeStraightScore(Graphics2D graphics2D, YachtScoreBoard scoreBoard, boolean isOpponent) {
        drawNumber(graphics2D, scoreBoard.getLargeStraight(), yachtLocationFactory.getLargeStraightHouseLocation(), isOpponent);
    }

    private void drawYachtScore(Graphics2D graphics2D, YachtScoreBoard scoreBoard, boolean isOpponent) {
        drawNumber(graphics2D, scoreBoard.getYacht(), yachtLocationFactory.getYachtLocation(), isOpponent);
    }

    private void drawTotalScores(Graphics2D graphics2D, YachtScoreBoard scoreBoard, boolean isOpponent) {
        drawNumber(graphics2D, scoreBoard.getTotalScores(), yachtLocationFactory.getTotalPointsLocation(), isOpponent);
    }

    private void drawAllScores(Graphics2D g, YachtScoreBoard scoreBoard, boolean isOpponent) {
        drawNumberScores(g, scoreBoard, isOpponent);
        drawBonusScore(g, scoreBoard, isOpponent);
        drawChoiceScore(g, scoreBoard, isOpponent);
        drawFourOfAKindScore(g, scoreBoard, isOpponent);
        drawFullHouseScore(g, scoreBoard, isOpponent);
        drawSmallStraightScore(g, scoreBoard, isOpponent);
        drawLargeStraightScore(g, scoreBoard, isOpponent);
        drawYachtScore(g, scoreBoard, isOpponent);
        drawTotalScores(g, scoreBoard, isOpponent);
    }

    @Override
    @Transactional(readOnly = true)
    public InputStream getBoard(YachtRoom yachtRoom, boolean isNewFile) throws IOException {
        if (yachtRoom.getTurnCount() == 0 && yachtRoom.getRollCount() == 0)
            return fileService.getResourceStream(FILE_PATH, FILE_BOARD, FILE_EXT);
        File file = fileService.getFile(FILE_PATH, yachtRoom.getIdx() + "", FILE_EXT);
        if (!isNewFile && file.exists())
                return new FileInputStream(file);
        YachtScoreBoard accountScoreBoard = yachtRoom.getAccountScoreBoard();
        YachtScoreBoard opponentScoreBoard = yachtRoom.getOpponentScoreBoard();

        // Board Image
        BufferedImage bufferedImageBoard = fileService.getBufferedResourceImage(FILE_PATH, FILE_BOARD, FILE_EXT);

        // Board Graphics
        Graphics2D graphics2D = bufferedImageBoard.createGraphics();

        // Font Init
        graphics2D.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );
        graphics2D.setFont(fontFactory.getArialFont30f());
        graphics2D.setColor(Color.BLACK);

        /*
        Dice
        */

        List<BufferedImage> bufferedDiceImages = new ArrayList<>();
        for (String name : FILE_DICES_NAMES)
            bufferedDiceImages.add(fileService.getBufferedResourceImage(FILE_PATH, FILE_DICES_PREFIX + name, FILE_EXT));

        // Selected Dice
        for (int i = 0; i < yachtRoom.getSelectedDices().size(); i++)
            drawImage(graphics2D, bufferedDiceImages.get(yachtRoom.getSelectedDices().get(i) - 1), yachtLocationFactory.getSelectedDiceLocation(i + 1));

        // Non Selected Dice
        for (int i = 0; i < yachtRoom.getNonSelectedDices().size(); i++)
            drawImage(graphics2D, bufferedDiceImages.get(yachtRoom.getNonSelectedDices().get(i) - 1), yachtLocationFactory.getRandomDiceLocation(i + 1));

        /*
        Score Board
        */

        for (int i = 0; i < 2; i++) {
            boolean isOpponent = (i != 0);
            drawAllScores(graphics2D, (isOpponent ? opponentScoreBoard : accountScoreBoard), isOpponent);
        }

        // Memory
        graphics2D.dispose();

        /*
        File
        */

        // Resize
        bufferedImageBoard = resize(bufferedImageBoard, FILE_WIDTH, FILE_HEIGHT);

        // Save File
        file.mkdirs();
        ImageIO.write(bufferedImageBoard, FILE_EXT, file);

        return new FileInputStream(file);
    }
}
