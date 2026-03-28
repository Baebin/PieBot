package com.piebin.piebot.global.service.impl.commands;

import com.piebin.piebot.global.domain.Account;
import com.piebin.piebot.global.entity.Sentence;
import com.piebin.piebot.global.service.impl.scheduler.MoneySchedulerServiceImpl;
import com.piebin.piebot.global.entity.UniEmoji;
import com.piebin.piebot.global.service.PageService;
import com.piebin.piebot.global.service.PieCommand;
import com.piebin.piebot.global.utility.CommandManager;
import com.piebin.piebot.global.utility.DateTimeManager;
import com.piebin.piebot.global.utility.NumberManager;
import com.piebin.piebot.global.utility.PageManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoneyRankCommand implements PieCommand, PageService {
    private final int OFFSET = 15;

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
        page = PageManager.getPage(MoneySchedulerServiceImpl.moneyRankList.size(), OFFSET, page);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Sentence.MONEY_RANK.getMessage() + " - " + page);
        embedBuilder.setColor(Color.GREEN);

        int from = (page - 1) * OFFSET + 1;
        int to = (page) * OFFSET;
        for (int rank = from; rank <= to; rank++)
            addField(embedBuilder, rank);
        embedBuilder.addField(Sentence.RANK_REFRESH.getMessage(), DateTimeManager.getDate(MoneySchedulerServiceImpl.moneyRankDateTime), false);
        return embedBuilder;
    }

    @Override
    @Transactional(readOnly = true)
    public void execute(MessageReceivedEvent event) {
        List<String> args = CommandManager.getArgs(event);

        int initPage = 1;
        if (args.size() >= 3) {
            int totalCnt = MoneySchedulerServiceImpl.moneyRankList.size();
            initPage = PageManager.getPage(totalCnt, OFFSET, args.get(2));
        }

        TextChannel channel = event.getChannel().asTextChannel();
        channel.sendMessageEmbeds(getPage(initPage).build()).queue((embed) -> {
            embed.addReaction(UniEmoji.ARROW_LEFT_DOUBLE.getEmoji()).queue();
            embed.addReaction(UniEmoji.ARROW_LEFT.getEmoji()).queue();
            embed.addReaction(UniEmoji.ARROW_REFRESH.getEmoji()).queue();
            embed.addReaction(UniEmoji.ARROW_RIGHT.getEmoji()).queue();
            embed.addReaction(UniEmoji.ARROW_RIGHT_DOUBLE.getEmoji()).queue();
        });
    }
}
