package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.domain.EasterEgg;
import com.piebin.piebot.model.domain.EasterEggHistory;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.model.repository.EasterEggHistoryRepository;
import com.piebin.piebot.model.repository.EasterEggRepository;
import com.piebin.piebot.service.PieCommand;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EasterEggListCommand implements PieCommand {
    private final EasterEggRepository easterEggRepository;
    private final EasterEggHistoryRepository easterEggHistoryRepository;

    @Override
    @Transactional(readOnly = true)
    public void execute(MessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Sentence.EASTER_EGG_LIST.getMessage());
        embedBuilder.setColor(Color.GREEN);

        long totalCnt = easterEggRepository.count();
        List<EasterEggHistory> histories = easterEggHistoryRepository.findAll();

        Map<Long, EasterEggHistory> historyMap = new HashMap<>();
        for (EasterEggHistory history : histories)
            historyMap.put(history.getEasterEgg().getIdx(), history);
        for (long i = 1; i <= totalCnt; i++) {
            EasterEggHistory history = historyMap.getOrDefault(i, null);
            if (history == null)
                embedBuilder.addField(i + ". ???", "미발견", true);
            else {
                EasterEgg easterEgg = history.getEasterEgg();
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
                String description = "[" + history.getRegDate().format(dateFormat) + "] " + history.getAccount().getName();
                embedBuilder.addField(i + ". " + easterEgg.getTitle(), description, true);
            }
        }
        long empty = (3 - totalCnt % 3);
        if (totalCnt % 3 != 0)
            for (long i = 1; i <= empty; i++)
                embedBuilder.addBlankField(true);
        TextChannel channel = event.getChannel().asTextChannel();;
        channel.sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
