package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.dto.embed.EmbedDto;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.utility.EmbedMessageHelper;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
@Service
public class TestCommand implements PieCommand {
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
            ClassPathResource resource = new ClassPathResource("omok/omok_board_skin_1.jpg");
            FileUpload fileUpload = FileUpload.fromData(resource.getFile());
            message.replyFiles(fileUpload).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
