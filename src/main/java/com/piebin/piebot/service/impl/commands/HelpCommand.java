package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.entity.CommandParameter;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.model.entity.UniEmoji;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.service.impl.CommandServiceImpl;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;

public class HelpCommand implements PieCommand {
    private void addField(EmbedBuilder embedBuilder, CommandParameter parameter) {
        String command = CommandServiceImpl.PREFIX + " " + Arrays.toString(parameter.getData());
        if (parameter.getArgs() != null)
            command += " " + parameter.getArgs();
        embedBuilder.addField(command, parameter.getDescription(), false);
    }

    public EmbedBuilder getPage(int page) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GREEN);

        switch (page) {
            case 1:
                embedBuilder.setTitle(Sentence.HELP_1.getMessage());
                addField(embedBuilder, CommandParameter.HELP);
                break;
            case 2:
                embedBuilder.setTitle(Sentence.HELP_2.getMessage());
                addField(embedBuilder, CommandParameter.BABO);
                addField(embedBuilder, CommandParameter.DICE);
                break;
            case 3:
                embedBuilder.setTitle(Sentence.HELP_3.getMessage());
                addField(embedBuilder, CommandParameter.SECRET_EASTEREGG);
                addField(embedBuilder, CommandParameter.SECRET_EASTEREGG_LIST);
                break;
            default:
                break;
        }
        return embedBuilder;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        TextChannel channel = event.getChannel().asTextChannel();
        Message message = channel.sendMessageEmbeds(getPage(1).build()).complete();

        message.addReaction(UniEmoji.NUM_1.getEmoji()).queue();
        message.addReaction(UniEmoji.NUM_2.getEmoji()).queue();
        message.addReaction(UniEmoji.NUM_3.getEmoji()).queue();
    }
}
