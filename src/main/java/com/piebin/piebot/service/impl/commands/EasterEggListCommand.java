package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.domain.EasterEgg;
import com.piebin.piebot.model.domain.EasterEggHistory;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.model.repository.EasterEggHistoryRepository;
import com.piebin.piebot.model.repository.EasterEggRepository;
import com.piebin.piebot.service.PageService;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.utility.CommandManager;
import com.piebin.piebot.utility.DateTimeManager;
import com.piebin.piebot.utility.EmojiManager;
import com.piebin.piebot.utility.PageManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EasterEggListCommand implements PieCommand, PageService {
    private final EasterEggRepository easterEggRepository;
    private final EasterEggHistoryRepository easterEggHistoryRepository;

    private void addField(EmbedBuilder embedBuilder, long i, EasterEggHistory history) {
        if (history == null)
            embedBuilder.addField(i + ". ???", "미발견", true);
        else {
            EasterEgg easterEgg = history.getEasterEgg();
            String time = DateTimeManager.getDate(history.getRegDate());
            String description = "[" + time + "] " + history.getAccount().getName();
            embedBuilder.addField(i + ". " + easterEgg.getTitle(), description, true);
        }
    }

    @Transactional(readOnly = true)
    public EmbedBuilder getPage(int page) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Sentence.EASTER_EGG_LIST.getMessage() + " - " + page);
        embedBuilder.setColor(Color.GREEN);

        long totalCnt = easterEggRepository.count();
        List<EasterEggHistory> histories = easterEggHistoryRepository.findAll();

        Map<Long, EasterEggHistory> historyMap = new HashMap<>();
        for (EasterEggHistory history : histories)
            historyMap.put(history.getEasterEgg().getIdx(), history);

        int from = (page - 1) * 15 + 1;
        int to = (page) * 15;
        int inTotalCnt = 0;
        for (long i = from; i <= to && i <= totalCnt; i++) {
            EasterEggHistory history = historyMap.getOrDefault(i, null);
            addField(embedBuilder, i, history);
            inTotalCnt++;
        }
        long empty = (3 - inTotalCnt % 3);
        if (inTotalCnt % 3 != 0)
            for (long i = 1; i <= empty; i++)
                embedBuilder.addBlankField(true);
        return embedBuilder;
    }

    @Override
    @Transactional(readOnly = true)
    public void execute(MessageReceivedEvent event) {
        List<String> args = CommandManager.getArgs(event);

        int initPage = 1;
        int totalCnt = (int) easterEggRepository.count();
        if (args.size() >= 3)
            initPage = PageManager.getPage(totalCnt, 15, args.get(2));

        TextChannel channel = event.getChannel().asTextChannel();
        Message message = channel.sendMessageEmbeds(getPage(initPage).build()).complete();

        int pages = PageManager.getPages(totalCnt, 15);
        for (int i = 1; i <= pages; i++)
            message.addReaction(EmojiManager.getEmoji(i)).queue();
    }
}
