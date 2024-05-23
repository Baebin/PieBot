package com.piebin.piebot.utility;

import com.piebin.piebot.model.entity.CommandErrorSentence;
import com.piebin.piebot.model.entity.Sentence;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class CommandHelper {
    public static List<String> getArgs(MessageReceivedEvent event) {
        return Arrays.asList(event.getMessage().getContentRaw().split(" "));
    }

    public static void printEmbedMessage(TextChannel channel, String title, String name, String value, Color color) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.addField(name, value, false);
        embedBuilder.setColor(color);
        channel.sendMessageEmbeds(embedBuilder.build()).queue();
    }

    public static void printErrorMessage(TextChannel channel, String title, String name, String value) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.addField(name, value, false);
        embedBuilder.setColor(Color.RED);
        channel.sendMessageEmbeds(embedBuilder.build()).queue();
    }

    public static void printCommandErrorMessage(TextChannel channel, CommandErrorSentence sentence) {
        printErrorMessage(channel, sentence.getTitle(), sentence.getMessage(), sentence.getDescription());
    }
}
