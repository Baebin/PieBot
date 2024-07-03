package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.dto.embed.EmbedDto;
import com.piebin.piebot.service.ImageService;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.utility.EmbedMessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestCommand implements PieCommand {
    private final ImageService imageService;

    @Override
    public void execute(MessageReceivedEvent event) {
        Message message = event.getMessage();

        EmbedDto dto = new EmbedDto();
        dto.setTitle("PieBot");
        dto.setMessage("Test");
        dto.setDescription("Babo");
        dto.setColor(Color.CYAN);

        EmbedMessageHelper.replyEmbedMessage(message, dto);

        try {
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

            File file = imageService.getFile("omok", "test", "png");
            file.mkdirs();

            ImageIO.write(bufferedImage, "png", file);
            FileUpload fileUploadC = FileUpload.fromData(file);

            message.replyFiles(fileUploadC).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
