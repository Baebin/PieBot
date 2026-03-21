package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.entity.UniEmoji;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.model.repository.YachtRoomRepository;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.utility.CommandManager;
import com.piebin.piebot.utility.EmbedMessageHelper;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YachtCommand implements PieCommand {
    private final AccountRepository accountRepository;

    private final YachtRoomRepository yachtRoomRepository;

    @Override
    public void execute(MessageReceivedEvent event) {
        List<String> args = CommandManager.getArgs(event);
        if (args.size() >= 3) {
            if (args.get(2).equals("대전") || args.get(2).equalsIgnoreCase("pvp")) {
                pvp(event, args);
                return;
            }
        }
        EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.YACHT_ARG1);
    }

    private void pvp(MessageReceivedEvent event, List<String> args) {
        if (args.size() >= 4) {
            String userId = CommandManager.getMentionId(args.get(3));
            if (event.getAuthor().getId().equals(userId)) {
                EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.YACHT_PVP_SELF);
                return;
            }
            Optional<Account> optionalFrom = accountRepository.findById(event.getAuthor().getId());
            Optional<Account> optionalTo = accountRepository.findById(userId);
            if (optionalFrom.isPresent() && optionalTo.isPresent()) {
                Account from = optionalFrom.get();
                boolean existsFrom = yachtRoomRepository.existsByAccountOrOpponent(from, from);
                if (existsFrom) {
                    EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.YACHT_PVP_EXISTS_FROM);
                    return;
                }
                Account to = optionalTo.get();
                boolean existsTo = yachtRoomRepository.existsByAccountOrOpponent(to, to);
                if (existsTo) {
                    EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.YACHT_PVP_EXISTS_TO);
                    return;
                }
                Message message = EmbedMessageHelper.replyCommandMessage(event.getMessage(), CommandSentence.YACHT_PVP, Color.GREEN);
                message.addReaction(UniEmoji.CHECK.getEmoji()).queue();
                return;
            }
        }
        EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.YACHT_PVP_ARG2);
    }
}
