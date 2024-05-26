package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.entity.CommandParameter;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.service.PageService;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.service.impl.CommandServiceImpl;
import com.piebin.piebot.service.impl.scheduler.MoneySchedulerServiceImpl;
import com.piebin.piebot.utility.CommandManager;
import com.piebin.piebot.utility.EmojiManager;
import com.piebin.piebot.utility.PageManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class HelpCommand implements PieCommand, PageService {
    public static int PAGES = 4;

    private void addField(EmbedBuilder embedBuilder, CommandParameter parameter) {
        String command = CommandServiceImpl.PREFIX + " " + Arrays.toString(parameter.getData());
        if (parameter.getArgs() != null)
            command += " " + parameter.getArgs();
        embedBuilder.addField(command, parameter.getDescription(), false);
    }

    @Override
    public EmbedBuilder getPage(int page) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GREEN);

        switch (page) {
            case 1:
                embedBuilder.setTitle(Sentence.HELP_1.getMessage());
                addField(embedBuilder, CommandParameter.PROFILE);
                addField(embedBuilder, CommandParameter.HELP);
                break;
            case 2:
                embedBuilder.setTitle(Sentence.HELP_2.getMessage());
                addField(embedBuilder, CommandParameter.BABO);
                addField(embedBuilder, CommandParameter.DICE);
                addField(embedBuilder, CommandParameter.OMOK_PVP);
                addField(embedBuilder, CommandParameter.OMOK_QUIT);
                addField(embedBuilder, CommandParameter.OMOK_PROFILE);
                addField(embedBuilder, CommandParameter.OMOK_CONTINUE);
                break;
            case 3:
                embedBuilder.setTitle(Sentence.HELP_3.getMessage());
                addField(embedBuilder, CommandParameter.PAY);
                addField(embedBuilder, CommandParameter.MONEY_RANK);
                break;
            case 4:
                embedBuilder.setTitle(Sentence.HELP_4.getMessage());
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
        List<String> args = CommandManager.getArgs(event);

        int initPage = 1;
        if (args.size() >= 3)
            initPage = PageManager.getPage(PAGES, args.get(2));

        TextChannel channel = event.getChannel().asTextChannel();
        Message message = channel.sendMessageEmbeds(getPage(initPage).build()).complete();

        for (int i = 1; i <= PAGES; i++)
            message.addReaction(EmojiManager.getEmoji(i)).queue();
    }
}
