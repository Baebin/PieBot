package com.piebin.piebot.service.impl;

import com.piebin.piebot.model.domain.OmokInfo;
import com.piebin.piebot.model.domain.OmokRoom;
import com.piebin.piebot.model.entity.OmokState;
import com.piebin.piebot.model.repository.OmokInfoRepository;
import com.piebin.piebot.service.ImageService;
import com.piebin.piebot.service.OmokSkinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OmokSkinServiceImpl implements OmokSkinService {
    private static final String auroraBoard = "omok_skin_aurora_board_x1024";
    private static final String auroraStoneBlack = "omok_skin_aurora_stone_1_x512";
    private static final String auroraStoneWhite = "omok_skin_aurora_stone_2_x512";

    private static final int auroraBoardSize = 1024;
    private static final int auroraStoneSize = 40;
    private static final int auroraStoneWeight = 49;
    private static final int auroraMaxNumber = 21;

    private final ImageService imageService;

    private final OmokInfoRepository omokInfoRepository;

    private int getPositionX(char x) {
        return (auroraBoardSize/2) + ((x - 'A' + 1) - (1 + auroraMaxNumber/2)) * auroraStoneWeight;
    }

    private int getPositionY(int y) {
        return (auroraBoardSize/2) + (y - (1 + auroraMaxNumber/2)) * auroraStoneWeight;
    }

    @Override
    public File getBoard(OmokRoom omokRoom) {
        File file = imageService.getFile("omok", omokRoom.getIdx() + "", "png");
        if (!file.exists()) {
            ClassPathResource resource = new ClassPathResource("omok/" + auroraBoard + ".png");
            try {
                return resource.getFile();
            } catch (IOException e) {}
        }
        return file;
    }

    @Override
    @Transactional(readOnly = true)
    public void updateBoard(OmokRoom omokRoom, OmokState omokState, char x, int y) {
        File file = imageService.getFile("omok", omokRoom.getIdx() + "", "png");

        ClassPathResource resourceBoard = new ClassPathResource("omok/" + auroraBoard + ".png");
        ClassPathResource resourceBlackStone = new ClassPathResource("omok/" + auroraStoneBlack + ".png");
        ClassPathResource resourceWhiteStone = new ClassPathResource("omok/" + auroraStoneWhite + ".png");

        log.info("file: {}" ,file);
        if (file.exists()) {
            try {
                int pX = getPositionX(x), pY = getPositionY(y);
                log.info("px: {}, py: {}", pY, pY);

                BufferedImage bufferedImageBoard = ImageIO.read(file);
                BufferedImage bufferedImageStone = ImageIO.read((omokState == OmokState.BLACK ? resourceBlackStone : resourceWhiteStone).getFile());

                Graphics2D graphics2D = bufferedImageBoard.createGraphics();
                graphics2D.drawImage(bufferedImageStone, pX - (auroraStoneSize/2), pY - (auroraStoneSize/2), auroraStoneSize, auroraStoneSize, null);

                ImageIO.write(bufferedImageBoard, "png", file);
            } catch (Exception e) {}
        } else {
            try {
                BufferedImage bufferedImageBoard = ImageIO.read(resourceBoard.getFile());
                BufferedImage bufferedImageBlackStone = ImageIO.read(resourceBlackStone.getFile());
                BufferedImage bufferedImageWhiteStone = ImageIO.read(resourceWhiteStone.getFile());

                Graphics2D graphics2D = bufferedImageBoard.createGraphics();

                List<OmokInfo> omokInfos = omokInfoRepository.findAllByRoom(omokRoom);
                for (OmokInfo omokInfo : omokInfos) {
                    String position = omokInfo.getPosition();
                    int pX = getPositionX(position.charAt(0));
                    log.info("px: {}, py: {}", pX, position.substring(1));
                    int pY = getPositionY(Integer.parseInt(position.substring(1)));
                    log.info("px: {}, py: {}", pY, pY);
                    graphics2D.drawImage(
                            (omokInfo.getState() == OmokState.BLACK ? bufferedImageBlackStone : bufferedImageWhiteStone),
                            pX - (auroraStoneSize/2), pY - (auroraStoneSize/2), auroraStoneSize, auroraStoneSize, null);
                }
                file.mkdirs();
                ImageIO.write(bufferedImageBoard, "png", file);
                log.info("Created File: {}" ,file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
