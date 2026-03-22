package com.piebin.piebot.service.impl;

import com.piebin.piebot.component.DiscordBotInfo;
import com.piebin.piebot.exception.AccountException;
import com.piebin.piebot.model.entity.EmbedSentence;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.service.AccountService;
import com.piebin.piebot.service.AsyncService;
import com.piebin.piebot.service.ReactionService;
import com.piebin.piebot.service.impl.reactions.*;
import com.piebin.piebot.utility.EmbedMessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final DiscordBotInfo botInfo;

    private final AsyncService asyncService;
    private final AccountService accountService;

    private final HelpReactionAdd helpReactionAdd;
    private final PatchNoteReactionAdd patchNoteReactionAdd;
    private final MoneyRankReactionAdd moneyRankReactionAdd;
    private final AttendanceRankReactionAdd attendanceRankReactionAdd;
    private final OmokReactionAdd omokReactionAdd;
    private final OmokRankReactionAdd omokRankReactionAdd;
    private final YachtReactionAdd yachtReactionAdd;
    private final MukchibaReactionAdd mukchibaReactionAdd;
    private final HorseRacingReactionAdd horseRacingReactionAdd;
    private final ShopReactionAdd shopReactionAdd;
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
        if (embed == null) {
            runNonEmbedEvent(event);
            return;
        }
        String title = embed.getTitle();
        Map<String, Consumer<MessageReactionAddEvent>> reactionMap = Map.ofEntries(
                Map.entry(Sentence.HELP.getMessage(), helpReactionAdd::execute),
                Map.entry(Sentence.PATCH_NOTE.getMessage(), patchNoteReactionAdd::execute),
                Map.entry(Sentence.MONEY_RANK.getMessage(), moneyRankReactionAdd::execute),
                Map.entry(Sentence.ATTENDANCE_RANK.getMessage(), attendanceRankReactionAdd::execute),
                Map.entry(Sentence.OMOK_RANK.getMessage(), omokRankReactionAdd::execute),
                Map.entry(Sentence.OMOK.getMessage(), omokReactionAdd::execute),
                Map.entry(Sentence.YACHT.getMessage(), yachtReactionAdd::execute),
                Map.entry(Sentence.GAMBLING_MUKCHIBA.getMessage(), mukchibaReactionAdd::execute),
                Map.entry(Sentence.GAMBLING_HORSE_RACING.getMessage(), horseRacingReactionAdd::execute),
                Map.entry(Sentence.SHOP.getMessage(), shopReactionAdd::execute),
                Map.entry(Sentence.CONTRIBUTOR.getMessage(), contributorReactionAdd::execute),
                Map.entry(Sentence.EASTER_EGG_LIST.getMessage(), easterEggListReactionAdd::execute)
        );
        for (Map.Entry<String, Consumer<MessageReactionAddEvent>> entry : reactionMap.entrySet()) {
            if (title.startsWith(entry.getKey())) {
                entry.getValue().accept(event);
                return;
            }
        }
        if (title.startsWith(Sentence.REGISTER.getMessage()))
            register(event, channel, message, userId);
    }

    void register(MessageReactionAddEvent event, TextChannel channel, Message message, String userId) {
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
    }

    void runNonEmbedEvent(MessageReactionAddEvent event) {
        Message message = event.retrieveMessage().complete();

        if (!message.getMentions().isMentioned(event.getUser()))
            return;
        List<String> raws = Arrays.asList(message.getContentRaw().split("\n"));
        log.info("{}", raws);
        if (raws.size() < 2)
            return;
        if (!raws.get(0).contains("vs"))
            return;
        if (!raws.get(1).contains("현재 차례"))
            return;

        // Remove Emoji
        asyncService.runAsync(() -> {
            User user = event.getUser();
            MessageReaction reaction = event.getReaction();
            reaction.removeReaction(user).queue();
        });
        yachtReactionAdd.executeWithNonEmbed(event);
    }
}
