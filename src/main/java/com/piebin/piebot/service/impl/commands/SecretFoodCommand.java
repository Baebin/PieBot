package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.utility.CommandManager;
import com.piebin.piebot.utility.EmbedMessageHelper;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class SecretFoodCommand implements PieCommand {
    @Override
    public void execute(MessageReceivedEvent event) {
        Message message = event.getMessage();
        List<String> args = CommandManager.getArgs(event);
        if (args.size() > 2) {
            String food = args.get(2);
            if (food.contains("김치치즈우동"))
                EmbedMessageHelper.replyCommandMessage(message, CommandSentence.SECRET_FOOD_SUCCESS, Color.GREEN);
            else if (food.contains("힌트") && food.contains("1"))
                EmbedMessageHelper.replyCommandMessage(message, CommandSentence.SECRET_FOOD_HINT1, Color.CYAN);
            else if (food.contains("힌트") && food.contains("2"))
                EmbedMessageHelper.replyCommandMessage(message, CommandSentence.SECRET_FOOD_HINT2, Color.CYAN);
            else if (food.contains("힌트") && food.contains("3"))
                EmbedMessageHelper.replyCommandMessage(message, CommandSentence.SECRET_FOOD_HINT3, Color.ORANGE);
            else if (food.contains("빈"))
                EmbedMessageHelper.replyCommandErrorMessage(message, CommandSentence.SECRET_FOOD_CONTAIN3);
            else if ((food.contains("김치") && food.contains("치즈")) && (food.contains("면") || food.contains("동")))
                EmbedMessageHelper.replyCommandMessage(message, CommandSentence.SECRET_FOOD_CONTAIN1, Color.ORANGE);
            else if (food.contains("코코넛"))
                EmbedMessageHelper.replyCommandErrorMessage(message, CommandSentence.SECRET_FOOD_CONTAIN2);
            else EmbedMessageHelper.replyCommandErrorMessage(message, CommandSentence.SECRET_FOOD_FAILED);
            return;
        }
        EmbedMessageHelper.replyCommandErrorMessage(message, CommandSentence.SECRET_FOOD_ARG1);
    }
}
