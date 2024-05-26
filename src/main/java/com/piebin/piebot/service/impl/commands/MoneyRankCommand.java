package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.model.entity.UniEmoji;
import com.piebin.piebot.service.PageService;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.service.impl.scheduler.MoneySchedulerServiceImpl;
import com.piebin.piebot.utility.CommandManager;
import com.piebin.piebot.utility.DateTimeManager;
import com.piebin.piebot.utility.NumberManager;
import com.piebin.piebot.utility.PageManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;

@Service
@Component
@RequiredArgsConstructor
public class MoneyRankCommand implements PieCommand, PageService {
    private void addField(EmbedBuilder embedBuilder, int rank) {
        if (MoneySchedulerServiceImpl.moneyRankList.size() < rank) {
            embedBuilder.addBlankField(true);
            return;
        }
        Account account = MoneySchedulerServiceImpl.moneyRankList.get(rank - 1);
        embedBuilder.addField(rank + "등. " + account.getName(), NumberManager.getNumber(account.getMoney()) + "빙", true);
    }

    @Override
    public EmbedBuilder getPage(int page) {
        page = PageManager.getPage(MoneySchedulerServiceImpl.moneyRankList.size(), 15, page);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Sentence.MONEY_RANK.getMessage() + " - " + page);
        embedBuilder.setColor(Color.GREEN);

        int from = (page - 1) * 15 + 1;
        int to = (page) * 15;
        for (int rank = from; rank <= to; rank++)
            addField(embedBuilder, rank);
        embedBuilder.addField(Sentence.MONEY_RANK_REFRESH.getMessage(), DateTimeManager.getDate(MoneySchedulerServiceImpl.moneyRankDateTime), false);
        return embedBuilder;
    }

    @Override
    @Transactional(readOnly = true)
    public void execute(MessageReceivedEvent event) {
        List<String> args = CommandManager.getArgs(event);

        int initPage = 1;
        if (args.size() >= 3) {
            int totalCnt = MoneySchedulerServiceImpl.moneyRankList.size();
            initPage = PageManager.getPage(totalCnt, 10, args.get(2));
        }

        TextChannel channel = event.getChannel().asTextChannel();
        Message message = channel.sendMessageEmbeds(getPage(initPage).build()).complete();

        message.addReaction(UniEmoji.ARROW_LEFT_DOUBLE.getEmoji()).queue();
        message.addReaction(UniEmoji.ARROW_LEFT.getEmoji()).queue();
        message.addReaction(UniEmoji.ARROW_REFRESH.getEmoji()).queue();
        message.addReaction(UniEmoji.ARROW_RIGHT.getEmoji()).queue();
        message.addReaction(UniEmoji.ARROW_RIGHT_DOUBLE.getEmoji()).queue();
    }
}
