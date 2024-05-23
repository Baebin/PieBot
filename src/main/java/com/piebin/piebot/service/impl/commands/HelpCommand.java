package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.entity.CommandParameter;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.service.impl.CommandServiceImpl;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;

public class HelpCommand implements PieCommand {
    @Override
    public void execute(MessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("명령어 목록");
        embedBuilder.setColor(Color.GREEN);

        for (CommandParameter parameter : CommandParameter.values()) {
            if (parameter.getArgs() != null && parameter.getArgs().equals("Secret"))
                continue;
            String command = CommandServiceImpl.PREFIX + " " + Arrays.toString(parameter.getData());
            if (parameter.getArgs() != null)
                command += " " + parameter.getArgs();
            embedBuilder.addField(command, parameter.getDescription(), false);
        }
        TextChannel channel = event.getChannel().asTextChannel();
        channel.sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
