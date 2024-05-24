package com.piebin.piebot.utility;

import com.piebin.piebot.model.dto.embed.EmbedDto;
import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.entity.EmbedSentence;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmbedMessageHelper {
    public static Map<String, String> receiver = new HashMap<>();

    public static List<String> getArgs(MessageReceivedEvent event) {
        return Arrays.asList(event.getMessage().getContentRaw().split(" "));
    }

    public static EmbedBuilder getEmbedBuilder(String title, String name, String value, Color color) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.addField(name, value, false);
        embedBuilder.setColor(color);
        return embedBuilder;
    }

    public static EmbedBuilder getEmbedBuilder(EmbedSentence sentence, Color color) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(sentence.getTitle());
        embedBuilder.addField(sentence.getMessage(), sentence.getDescription(), false);
        embedBuilder.setColor(color);
        return embedBuilder;
    }

    public static Message printEmbedMessage(TextChannel channel, String title, String name, String value, Color color) {
        EmbedBuilder embedBuilder = getEmbedBuilder(title, name, value, color);
        return channel.sendMessageEmbeds(embedBuilder.build()).complete();
    }

    public static Message printEmbedMessage(TextChannel channel, EmbedDto dto) {
        return printEmbedMessage(channel, dto.getTitle(), dto.getMessage(), dto.getDescription(), dto.getColor());
    }

    public static Message printEmbedMessage(TextChannel channel, EmbedSentence sentence, Color color) {
        return printEmbedMessage(channel, sentence.getTitle(), sentence.getMessage(), sentence.getDescription(), color);
    }

    public static Message printCommandMessage(TextChannel channel, CommandSentence sentence, Color color) {
        return printEmbedMessage(channel, sentence.getTitle(), sentence.getMessage(), sentence.getDescription(), color);
    }

    public static Message printErrorMessage(TextChannel channel, String title, String name, String value) {
        return printEmbedMessage(channel, title, name, value, Color.RED);
    }

    public static Message printCommandErrorMessage(TextChannel channel, CommandSentence sentence) {
        return printErrorMessage(channel, sentence.getTitle(), sentence.getMessage(), sentence.getDescription());
    }
}
