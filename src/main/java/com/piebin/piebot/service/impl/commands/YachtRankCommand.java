package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.domain.Yacht;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.model.entity.UniEmoji;
import com.piebin.piebot.service.PageService;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.service.impl.scheduler.YachtSchedulerServiceImpl;
import com.piebin.piebot.utility.CommandManager;
import com.piebin.piebot.utility.DateTimeManager;
import com.piebin.piebot.utility.NumberManager;
import com.piebin.piebot.utility.PageManager;
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
public class YachtRankCommand implements PieCommand, PageService {
    private final int OFFSET = 10;

    private void addField(EmbedBuilder embedBuilder, int rank) {
        if (YachtSchedulerServiceImpl.yachtRankList.size() < rank) {
            embedBuilder.addBlankField(true);
            return;
        }
        Yacht yacht = YachtSchedulerServiceImpl.yachtRankList.get(rank - 1);
        long total = (yacht.getWin() + yacht.getTie() + yacht.getLose());
        double odds = 0.0;
        if (total != 0)
            odds = (100 * yacht.getWin() / total);
        String value = NumberManager.getNumber(yacht.getWin()) + "승 "
                + NumberManager.getNumber(yacht.getTie()) + "무 "
                + NumberManager.getNumber(yacht.getLose()) + "패 "
                + " (" + String.format("%.2f", odds) + "%)";
        embedBuilder.addField(rank + "등. " + yacht.getAccount().getName(), value, true);
    }

    @Override
    public EmbedBuilder getPage(int page) {
        page = PageManager.getPage(YachtSchedulerServiceImpl.yachtRankList.size(), OFFSET, page);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Sentence.OMOK_RANK.getMessage() + " - " + page);
        embedBuilder.setColor(Color.GREEN);

        int from = (page - 1) * OFFSET + 1;
        int to = (page) * OFFSET;
        for (int rank = from; rank <= to; rank++)
            addField(embedBuilder, rank);
        embedBuilder.addField(Sentence.RANK_REFRESH.getMessage(), DateTimeManager.getDate(YachtSchedulerServiceImpl.yachtRankDateTime), false);
        return embedBuilder;
    }

    @Override
    @Transactional(readOnly = true)
    public void execute(MessageReceivedEvent event) {
        List<String> args = CommandManager.getArgs(event);

        int initPage = 1;
        if (args.size() >= 4) {
            int totalCnt = YachtSchedulerServiceImpl.yachtRankList.size();
            initPage = PageManager.getPage(totalCnt, OFFSET, args.get(3));
        }

        TextChannel channel = event.getChannel().asTextChannel();
        channel.sendMessageEmbeds(getPage(initPage).build()).queue((message) -> {
            message.addReaction(UniEmoji.ARROW_LEFT_DOUBLE.getEmoji()).queue();
            message.addReaction(UniEmoji.ARROW_LEFT.getEmoji()).queue();
            message.addReaction(UniEmoji.ARROW_REFRESH.getEmoji()).queue();
            message.addReaction(UniEmoji.ARROW_RIGHT.getEmoji()).queue();
            message.addReaction(UniEmoji.ARROW_RIGHT_DOUBLE.getEmoji()).queue();
        });
    }
}
