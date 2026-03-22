package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.service.YachtCacheService;
import com.piebin.piebot.service.YachtService;
import com.piebin.piebot.utility.CommandManager;
import com.piebin.piebot.utility.EmbedMessageHelper;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YachtCommand implements PieCommand {
    private final YachtService yachtService;
    private final YachtCacheService yachtCacheService;

    @Override
    @Transactional
    public void execute(MessageReceivedEvent event) {
        List<String> args = CommandManager.getArgs(event);
        if (args.size() >= 3) {
            if (args.get(2).equals("대전") || args.get(2).equalsIgnoreCase("pvp")) {
                yachtService.invitePVP(event);
                return;
            }
            if (args.get(2).equals("퇴장") || args.get(2).equalsIgnoreCase("quit")) {
                yachtService.quitYachtRoom(event);
                return;
            }
            if (args.get(2).equals("이어하기") || args.get(2).equalsIgnoreCase("continue")) {
                yachtService.continueYachtRoom(event);
                return;
            }
        }
        if (yachtCacheService.hasCache(event.getMessage().getAuthor().getId()))
            return;
        EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.YACHT_ARG1);
    }
}
