package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.domain.YachtRoom;
import com.piebin.piebot.model.dto.embed.EmbedDto;
import com.piebin.piebot.service.FileService;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.service.YachtDrawingService;
import com.piebin.piebot.utility.CommandManager;
import com.piebin.piebot.utility.EmbedMessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestCommand implements PieCommand {
    private final FileService fileService;
    private final YachtDrawingService yachtDrawingService;

    private final EmbedMessageHelper embedMessageHelper;

    @Override
    public void execute(MessageReceivedEvent event) {
        Message message = event.getMessage();

        EmbedDto dto = new EmbedDto();
        dto.setTitle("PieBot");
        dto.setMessage("Test");
        dto.setDescription("Babo");
        dto.setColor(Color.CYAN);

        embedMessageHelper.replyEmbedMessage(message, dto);

        List<String> args = CommandManager.getArgs(event);

        try {
            if (args.size() < 3)
                return;
            switch (args.get(2)) {
                case "o":
                    testOmokCommand(message);
                    break;
                case "y":
                    testYachtCommand(message);
                    break;
                case "y2":
                    testYachtCommandB(message);
                    break;
                case "y3":
                    testYachtCommandC(message);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();

            log.info("user: {}, trace: {}", event.getAuthor(), e.getStackTrace());
        }
    }

    private void testOmokCommand(Message message) throws IOException {
        ClassPathResource resource = new ClassPathResource("omok/omok_skin_aurora_board_x1024.png");
        FileUpload fileUpload = FileUpload.fromData(resource.getFile());

        ClassPathResource resourceA = new ClassPathResource("omok/omok_skin_aurora_stone_1_x512.png");
        FileUpload fileUploadA = FileUpload.fromData(resourceA.getFile());

        ClassPathResource resourceB = new ClassPathResource("omok/omok_skin_aurora_stone_2_x512.png");
        FileUpload fileUploadB = FileUpload.fromData(resourceB.getFile());

        BufferedImage bufferedImage = ImageIO.read(resource.getFile());
        BufferedImage bufferedImageA = ImageIO.read(resourceA.getFile());
        BufferedImage bufferedImageB = ImageIO.read(resourceB.getFile());

        Graphics2D graphics2D = bufferedImage.createGraphics();

        int size = 40;
        int weight = 49;
        graphics2D.drawImage(bufferedImageA, 512 - (size/2), 512 - size/2, size, size, null);
        for (int i = 512, j = 0; j <= 10; i += weight, j++) {
            graphics2D.drawImage(bufferedImageB, i - (size/2), 512 - (size/2), size, size, null);
            graphics2D.drawImage(bufferedImageB, 512 - (size/2), i - (size/2), size, size, null);
        }
        for (int i = 512 - weight, j = 0; j <= 9; i -= weight, j++) {
            graphics2D.drawImage(bufferedImageB, i - (size/2), 512 - (size/2), size, size, null);
            graphics2D.drawImage(bufferedImageB, 512 - (size/2), i - (size/2), size, size, null);
        }

        File file = fileService.getFile("omok", "test", "png");
        file.mkdirs();

        ImageIO.write(bufferedImage, "png", file);
        FileUpload fileUploadC = FileUpload.fromData(file);

        message.replyFiles(fileUploadC).queue();
    }

    private void testYachtCommand(Message message) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("yacht/board.png");
        FileUpload fileUpload = FileUpload.fromData(classPathResource.getFile());
        message.replyFiles(fileUpload).queue();

        InputStream inputStream = fileService.getResourceStream("yacht", "board", "png");
        FileUpload fileUploadA = FileUpload.fromData(inputStream, "yacht.png");
        message.replyFiles(fileUploadA).queue();

        Resource resource = fileService.getResource("yacht", "board", "png");
        FileUpload fileUploadB = FileUpload.fromData(resource.getFile());
        message.replyFiles(fileUploadB).queue();
    }

    private void testYachtCommandB(Message message) throws IOException {
        YachtRoom yachtRoom = YachtRoom.builder()
                .idx(-1L)
                .build();
        FileUpload fileUpload = FileUpload.fromData(
                yachtDrawingService.getBoard(yachtRoom, true), "yacht.png");
        message.replyFiles(fileUpload).queue();

        yachtRoom.setRollCount(1);
        yachtRoom.setNonSelectedDices(Arrays.asList(1, 2, 3, 4, 5));
        FileUpload fileUploadB = FileUpload.fromData(
                yachtDrawingService.getBoard(yachtRoom, true), "yacht.png");
        message.replyFiles(fileUploadB).queue();

        FileUpload fileUploadC = FileUpload.fromData(
                yachtDrawingService.getBoard(yachtRoom, false), "yacht.png");
        message.replyFiles(fileUploadC).queue();
    }

    private void testYachtCommandC(Message message) throws IOException {
        BufferedImage bufferedImage = fileService.getBufferedResourceImage("yacht", "board", "png");

        File file = fileService.getFile("yacht", "test", "png");
        file.mkdirs();

        ImageIO.write(bufferedImage, "png", file);
        FileUpload fileUpload = FileUpload.fromData(file);
        message.replyFiles(fileUpload).queue();
    }
}
