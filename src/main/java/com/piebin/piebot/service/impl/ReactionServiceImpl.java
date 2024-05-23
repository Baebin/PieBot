package com.piebin.piebot.service.impl;

import com.piebin.piebot.exception.AccountException;
import com.piebin.piebot.model.entity.EmbedSentence;
import com.piebin.piebot.service.AccountService;
import com.piebin.piebot.service.ReactionService;
import com.piebin.piebot.utility.EmbedMessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

import java.awt.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final AccountService accountService;

    @Override
    public void run(MessageReactionAddEvent event) {
        String messageId = event.getMessageId();
        String userId = EmbedMessageHelper.receiver.getOrDefault(messageId, null);
        if (userId == null)
            return;
        if (!userId.equals(event.getUserId()))
            return;
        TextChannel channel = event.getChannel().asTextChannel();
        Message message = channel.retrieveMessageById(messageId).complete();
        try {
            accountService.register(event.getGuild().retrieveMemberById(userId).complete(), channel);
            message.editMessageEmbeds(
                    EmbedMessageHelper.getEmbedBuilder( EmbedSentence.REGISTER_COMPLETED, Color.GREEN).build()
            ).queue();
        } catch (AccountException e) {
            message.editMessageEmbeds(
                    EmbedMessageHelper.getEmbedBuilder(EmbedSentence.REGISTER_ALREADY_EXISTS, Color.RED).build()
            ).queue();
        }
        EmbedMessageHelper.receiver.remove(messageId);
    }
}
