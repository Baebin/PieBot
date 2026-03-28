package com.piebin.piebot.global.utility.impl;

import com.piebin.piebot.global.entity.CommandSentence;
import com.piebin.piebot.global.dto.embed.EmbedDto;
import com.piebin.piebot.global.entity.EmbedSentence;
import com.piebin.piebot.global.utility.EmbedMessageHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.function.Consumer;

@Component
public class EmbedMessageHelperImpl implements EmbedMessageHelper {
    @Override
    public EmbedBuilder getEmbedBuilder(String title, String name, String value, Color color) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.addField(name, value, false);
        embedBuilder.setColor(color);
        return embedBuilder;
    }

    @Override
    public EmbedBuilder getEmbedBuilder(EmbedSentence sentence, Color color) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(sentence.getTitle());
        embedBuilder.addField(sentence.getMessage(), sentence.getDescription(), false);
        embedBuilder.setColor(color);
        return embedBuilder;
    }

    @Override
    public EmbedBuilder getEmbedBuilder(CommandSentence sentence, Color color) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(sentence.getTitle());
        embedBuilder.addField(sentence.getMessage(), sentence.getDescription(), false);
        embedBuilder.setColor(color);
        return embedBuilder;
    }

    @Override
    public EmbedBuilder getEmbedBuilder(EmbedDto dto) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(dto.getTitle());
        embedBuilder.addField(dto.getMessage(), dto.getDescription(), false);
        embedBuilder.setColor(dto.getColor());
        return embedBuilder;
    }

    /*
    Reply
    */
    @Override
    public void replyEmbedMessage(Message message, String title, String name, String value, Color color) {
        EmbedBuilder embedBuilder = getEmbedBuilder(title, name, value, color);
        message.replyEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public void replyEmbedMessage(Message message, String title, String name, String value, Color color, Consumer<Message> consumer) {
        EmbedBuilder embedBuilder = getEmbedBuilder(title, name, value, color);
        message.replyEmbeds(embedBuilder.build()).queue((embed) -> consumer.accept(embed));
    }

    @Override
    public void replyEmbedMessage(Message message, EmbedDto dto) {
        EmbedBuilder embedBuilder = getEmbedBuilder(dto.getTitle(), dto.getMessage(), dto.getDescription(), dto.getColor());
        message.replyEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public void replyEmbedMessage(Message message, EmbedDto dto, Consumer<Message> consumer) {
        replyEmbedMessage(message, dto.getTitle(), dto.getMessage(), dto.getDescription(), dto.getColor(), consumer);
    }

    @Override
    public void replyEmbedMessage(Message message, EmbedSentence sentence, Color color) {
        replyEmbedMessage(message, sentence.getTitle(), sentence.getMessage(), sentence.getDescription(), color);
    }

    @Override
    public void replyEmbedMessage(Message message, EmbedSentence sentence, Color color, Consumer<Message> consumer) {
        replyEmbedMessage(message, sentence.getTitle(), sentence.getMessage(), sentence.getDescription(), color, consumer);
    }

    @Override
    public void replyCommandMessage(Message message, CommandSentence sentence, Color color) {
        replyEmbedMessage(message, sentence.getTitle(), sentence.getMessage(), sentence.getDescription(), color);
    }

    @Override
    public void replyCommandMessage(Message message, CommandSentence sentence, Color color, Consumer<Message> consumer) {
        replyEmbedMessage(message, sentence.getTitle(), sentence.getMessage(), sentence.getDescription(), color, consumer);
    }

    @Override
    public void replyErrorMessage(Message message, String title, String name, String value) {
        replyEmbedMessage(message, title, name, value, Color.RED);
    }

    @Override
    public void replyErrorMessage(Message message, String title, String name, String value, Consumer<Message> consumer) {
        replyEmbedMessage(message, title, name, value, Color.RED, consumer);
    }

    @Override
    public void replyCommandErrorMessage(Message message, CommandSentence sentence) {
        replyEmbedMessage(message, sentence.getTitle(), sentence.getMessage(), sentence.getDescription(), Color.RED);
    }

    @Override
    public void replyCommandErrorMessage(Message message, CommandSentence sentence, Consumer<Message> consumer) {
        replyEmbedMessage(message, sentence.getTitle(), sentence.getMessage(), sentence.getDescription(), Color.RED, consumer);
    }

    /*
    Print
    */
    @Override
    public void printEmbedMessage(TextChannel channel, String title, String name, String value, Color color) {
        EmbedBuilder embedBuilder = getEmbedBuilder(title, name, value, color);
        channel.sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public void printEmbedMessage(TextChannel channel, EmbedDto dto) {
        printEmbedMessage(channel, dto.getTitle(), dto.getMessage(), dto.getDescription(), dto.getColor());
    }

    @Override
    public void printEmbedMessage(TextChannel channel, EmbedSentence sentence, Color color) {
        printEmbedMessage(channel, sentence.getTitle(), sentence.getMessage(), sentence.getDescription(), color);
    }

    @Override
    public void printCommandMessage(TextChannel channel, CommandSentence sentence, Color color) {
        printEmbedMessage(channel, sentence.getTitle(), sentence.getMessage(), sentence.getDescription(), color);
    }

    @Override
    public void printErrorMessage(TextChannel channel, String title, String name, String value) {
        printEmbedMessage(channel, title, name, value, Color.RED);
    }

    @Override
    public void printCommandErrorMessage(TextChannel channel, CommandSentence sentence) {
        printErrorMessage(channel, sentence.getTitle(), sentence.getMessage(), sentence.getDescription());
    }
}
