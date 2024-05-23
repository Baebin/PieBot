package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.utility.EmbedMessageHelper;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

@AllArgsConstructor
public class EmbedPrintCommand implements PieCommand {
    private CommandSentence sentence;
    private Color color;

    @Override
    public void execute(MessageReceivedEvent event) {
        TextChannel channel = event.getChannel().asTextChannel();
        EmbedMessageHelper.printCommandMessage(channel, sentence, color);
    }
}
