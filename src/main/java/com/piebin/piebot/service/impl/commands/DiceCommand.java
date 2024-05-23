package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.utility.EmbedMessageHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class DiceCommand implements PieCommand {
    @Override
    public void execute(MessageReceivedEvent event) {
        TextChannel channel = event.getChannel().asTextChannel();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("주사위 결과");
        embedBuilder.setColor(Color.GREEN);

        List<String> args = EmbedMessageHelper.getArgs(event);
        if (args.size() >= 2) {
            try {
                int range = Integer.parseInt(args.get(2));
                String message = Sentence.COMMAND_DICE_RESULT.getMessage() + " : " + (new Random().nextInt(range) + 1);
                channel.sendMessage(message).queue();
                return;
            } catch (Exception e) {}
        }
        EmbedMessageHelper.printCommandErrorMessage(channel, CommandSentence.DICE_ARG1);
    }
}
