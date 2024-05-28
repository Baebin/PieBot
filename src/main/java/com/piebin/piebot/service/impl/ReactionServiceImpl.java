package com.piebin.piebot.service.impl;

import com.piebin.piebot.component.DiscordBotInfo;
import com.piebin.piebot.exception.AccountException;
import com.piebin.piebot.model.entity.EmbedSentence;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.service.AccountService;
import com.piebin.piebot.service.ReactionService;
import com.piebin.piebot.service.impl.reactions.*;
import com.piebin.piebot.utility.EmbedMessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

import java.awt.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final DiscordBotInfo botInfo;

    private final AccountService accountService;

    private final HelpReactionAdd helpReactionAdd;
    private final PatchNoteReactionAdd patchNoteReactionAdd;
    private final MoneyRankReactionAdd moneyRankReactionAdd;
    private final OmokReactionAdd omokReactionAdd;
    private final OmokRankReactionAdd omokRankReactionAdd;
    private final ContributorReactionAdd contributorReactionAdd;
    private final EasterEggListReactionAdd easterEggListReactionAdd;

    @Override
    public void run(MessageReactionAddEvent event) {
        User user = event.retrieveUser().complete();
        if (user.isBot())
            return;
        String authorId = event.getMessageAuthorId();
        if (!authorId.equals(botInfo.getBotId()))
            return;
        String userId = event.getUserId();

        TextChannel channel = event.getChannel().asTextChannel();
        Message message = event.retrieveMessage().complete();

        MessageEmbed embed = null;
        try {
            embed = message.getEmbeds().get(0);
        } catch (IndexOutOfBoundsException e) {}
        if (embed == null)
            return;
        String title = embed.getTitle();
        if (title.startsWith(Sentence.HELP.getMessage())) {
            helpReactionAdd.execute(event);
            return;
        }
        if (title.startsWith(Sentence.PATCH_NOTE.getMessage())) {
            patchNoteReactionAdd.execute(event);
            return;
        }
        if (title.startsWith(Sentence.MONEY_RANK.getMessage())) {
            moneyRankReactionAdd.execute(event);
            return;
        }
        if (title.startsWith(Sentence.OMOK_RANK.getMessage())) {
            omokRankReactionAdd.execute(event);
            return;
        }
        if (title.startsWith(Sentence.OMOK.getMessage())) {
            omokReactionAdd.execute(event);
            return;
        }
        if (title.startsWith(Sentence.CONTRIBUTOR.getMessage())) {
            contributorReactionAdd.execute(event);
            return;
        }
        if (title.startsWith(Sentence.EASTER_EGG_LIST.getMessage())) {
            easterEggListReactionAdd.execute(event);
            return;
        }
        if (title.startsWith(Sentence.REGISTER.getMessage())) {
            Message rMessage = message.getReferencedMessage();
            if (rMessage == null)
                return;
            String receiverId = rMessage.getAuthor().getId();
            if (receiverId == null)
                return;
            if (!userId.equals(receiverId))
                return;
            try {
                Member member = event.retrieveMember().complete();
                accountService.register(channel, member);
                message.editMessageEmbeds(
                        EmbedMessageHelper.getEmbedBuilder(EmbedSentence.REGISTER_COMPLETED, Color.GREEN).build()
                ).queue();
            } catch (AccountException e) {
                message.editMessageEmbeds(
                        EmbedMessageHelper.getEmbedBuilder(EmbedSentence.REGISTER_ALREADY_EXISTS, Color.RED).build()
                ).queue();
            }
            return;
        }
    }
}
