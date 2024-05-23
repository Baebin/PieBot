package com.piebin.piebot.utility;

import com.piebin.piebot.model.entity.CommandSentence;
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

    public static void printCommandMessage(TextChannel channel, CommandSentence sentence, Color color) {
        printEmbedMessage(channel, sentence.getTitle(), sentence.getMessage(), sentence.getDescription(), color);
    }

    public static void printErrorMessage(TextChannel channel, String title, String name, String value) {
        printEmbedMessage(channel, title, name, value, Color.RED);
    }

    public static void printCommandErrorMessage(TextChannel channel, CommandSentence sentence) {
        printErrorMessage(channel, sentence.getTitle(), sentence.getMessage(), sentence.getDescription());
    }
}
