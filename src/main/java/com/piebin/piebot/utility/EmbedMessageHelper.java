package com.piebin.piebot.utility;

import com.piebin.piebot.model.dto.embed.EmbedDto;
import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.entity.EmbedSentence;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;
import java.util.function.Consumer;

public interface EmbedMessageHelper {
    EmbedBuilder getEmbedBuilder(String title, String name, String value, Color color);
    EmbedBuilder getEmbedBuilder(EmbedSentence sentence, Color color);
    EmbedBuilder getEmbedBuilder(CommandSentence sentence, Color color);
    EmbedBuilder getEmbedBuilder(EmbedDto dto);

    void replyEmbedMessage(Message message, String title, String name, String value, Color color);
    void replyEmbedMessage(Message message, String title, String name, String value, Color color, Consumer<Message> consumer);
    void replyEmbedMessage(Message message, EmbedDto dto);
    void replyEmbedMessage(Message message, EmbedDto dto, Consumer<Message> consumer);
    void replyEmbedMessage(Message message, EmbedSentence sentence, Color color);
    void replyEmbedMessage(Message message, EmbedSentence sentence, Color color, Consumer<Message> consumer);
    void replyCommandMessage(Message message, CommandSentence sentence, Color color);
    void replyCommandMessage(Message message, CommandSentence sentence, Color color, Consumer<Message> consumer);
    void replyErrorMessage(Message message, String title, String name, String value);
    void replyErrorMessage(Message message, String title, String name, String value, Consumer<Message> consumer);
    void replyCommandErrorMessage(Message message, CommandSentence sentence);
    void replyCommandErrorMessage(Message message, CommandSentence sentence, Consumer<Message> consumer);

    void printEmbedMessage(TextChannel channel, String title, String name, String value, Color color);
    void printEmbedMessage(TextChannel channel, EmbedDto dto);
    void printEmbedMessage(TextChannel channel, EmbedSentence sentence, Color color);
    void printCommandMessage(TextChannel channel, CommandSentence sentence, Color color);
    void printErrorMessage(TextChannel channel, String title, String name, String value);
    void printCommandErrorMessage(TextChannel channel, CommandSentence sentence);
}
