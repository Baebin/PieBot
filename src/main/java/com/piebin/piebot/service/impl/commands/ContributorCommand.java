package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.domain.Contributor;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.model.entity.UniEmoji;
import com.piebin.piebot.model.repository.ContributorRepository;
import com.piebin.piebot.service.PageService;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.utility.CommandManager;
import com.piebin.piebot.utility.PageManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContributorCommand implements PieCommand, PageService {
    private final int OFFSET = 10;

    private final ContributorRepository contributorRepository;

    @Override
    @Transactional(readOnly = true)
    public void execute(MessageReceivedEvent event) {
        List<String> args = CommandManager.getArgs(event);

        int initPage = 1;
        if (args.size() >= 3) {
            long totalCnt = contributorRepository.count();
            initPage = PageManager.getPage((int)totalCnt, OFFSET, args.get(2));
        }

        TextChannel channel = event.getChannel().asTextChannel();
        Message message = channel.sendMessageEmbeds(getPage(initPage).build()).complete();

        message.addReaction(UniEmoji.ARROW_LEFT_DOUBLE.getEmoji()).queue();
        message.addReaction(UniEmoji.ARROW_LEFT.getEmoji()).queue();
        message.addReaction(UniEmoji.ARROW_REFRESH.getEmoji()).queue();
        message.addReaction(UniEmoji.ARROW_RIGHT.getEmoji()).queue();
        message.addReaction(UniEmoji.ARROW_RIGHT_DOUBLE.getEmoji()).queue();
    }

    private void addField(EmbedBuilder embedBuilder, Contributor contributor) {
        embedBuilder.addField(
                contributor.getIdx() + ". " + contributor.getAccount().getName(),
                contributor.getDescription(), false);
    }

    @Override
    public EmbedBuilder getPage(int page) {
        page = PageManager.getPage((int)contributorRepository.count(), OFFSET, page);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Sentence.CONTRIBUTOR.getMessage() + " - " + page);
        embedBuilder.setColor(Color.GREEN);

        PageRequest pageRequest = PageRequest.of(page - 1, OFFSET);
        for (Contributor contributor : contributorRepository.findAll(pageRequest).getContent())
            addField(embedBuilder, contributor);
        return embedBuilder;
    }
}
