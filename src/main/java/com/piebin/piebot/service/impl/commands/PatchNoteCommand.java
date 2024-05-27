package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.domain.PatchNote;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.model.entity.UniEmoji;
import com.piebin.piebot.model.repository.PatchNoteRepository;
import com.piebin.piebot.service.PageService;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.utility.CommandManager;
import com.piebin.piebot.utility.DateTimeManager;
import com.piebin.piebot.utility.PageManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatchNoteCommand implements PieCommand, PageService {
    private final int OFFSET = 5;

    private final PatchNoteRepository patchNoteRepository;

    @Override
    @Transactional(readOnly = true)
    public void execute(MessageReceivedEvent event) {
        List<String> args = CommandManager.getArgs(event);

        int initPage = 1;
        if (args.size() >= 3) {
            long totalCnt = patchNoteRepository.count();
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

    private void addField(EmbedBuilder embedBuilder, PatchNote patchNote) {
        embedBuilder.addField(
                patchNote.getMessage(),
                DateTimeManager.getDate(patchNote.getRegDate()), true);
    }

    @Override
    public EmbedBuilder getPage(int page) {
        page = PageManager.getPage((int)patchNoteRepository.count(), OFFSET, page);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Sentence.PATCH_NOTE.getMessage() + " - " + page);
        embedBuilder.setColor(Color.GREEN);

        PageRequest pageRequest = PageRequest.of(page - 1, OFFSET);
        for (PatchNote patchNote : patchNoteRepository.findAll(pageRequest).getContent())
            addField(embedBuilder, patchNote);
        return embedBuilder;
    }
}
