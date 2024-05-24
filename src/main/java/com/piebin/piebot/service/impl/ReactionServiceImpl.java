package com.piebin.piebot.service.impl;

import com.piebin.piebot.component.DiscordBotInfo;
import com.piebin.piebot.exception.AccountException;
import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.entity.EmbedSentence;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.service.AccountService;
import com.piebin.piebot.service.ReactionService;
import com.piebin.piebot.service.impl.reactions.HelpReactionAdd;
import com.piebin.piebot.utility.EmbedMessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.awt.*;

@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final DiscordBotInfo botInfo;

    private final AccountService accountService;

    @Override
    public void run(MessageReactionAddEvent event) {
        User user = event.retrieveUser().complete();
        if (user.isBot())
            return;
        String authorId = event.getMessageAuthorId();
        if (!authorId.equals(botInfo.getBotId()))
            return;
        String userId = event.getUserId();
        String messageId = event.getMessageId();

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
            new HelpReactionAdd().execute(event);
            return;
        }
        if (title.startsWith(Sentence.REGISTER.getMessage())) {
            String receiverId = EmbedMessageHelper.receiver.getOrDefault(messageId, null);
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
            EmbedMessageHelper.receiver.remove(messageId);
            return;
        }

    }
}
