package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.dto.embed.EmbedDto;
import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.utility.CommandManager;
import com.piebin.piebot.utility.EmbedMessageHelper;
import com.piebin.piebot.utility.NumberManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayCommand implements PieCommand {
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void execute(MessageReceivedEvent event) {
        List<String> args = CommandManager.getArgs(event);
        log.info("args: {}", args);

        if (args.size() >= 3) {
            String userId = CommandManager.getMentionId(args.get(2));
            if (event.getAuthor().getId().equals(userId)) {
                EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.PAY_ARG1_SELF);
                return;
            }
            Optional<Account> optionalFrom = accountRepository.findById(event.getAuthor().getId());
            Optional<Account> optionalTo = accountRepository.findById(userId);
            if (optionalFrom.isPresent() && optionalTo.isPresent()) {
                if (args.size() >= 4) {
                    Account from = optionalFrom.get();
                    Account to = optionalTo.get();
                    try {
                        long money = Long.parseLong(args.get(3));
                        if (1 <= money) {
                            if (money <= from.getMoney()) {
                                from.setMoney(from.getMoney() - money);
                                to.setMoney(to.getMoney() + money);

                                EmbedDto dto = new EmbedDto(CommandSentence.PAY_COMPLETED, Color.GREEN);
                                dto.changeMessage(NumberManager.getNumber(money));
                                EmbedMessageHelper.replyEmbedMessage(event.getMessage(), dto);
                                return;
                            }
                            EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.PAY_ARG2_LESS);
                            return;
                        }
                    } catch (Exception e) {}
                }
                EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.PAY_ARG2_MIN);
                return;
            }
        }
        EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.PAY_ARG1);
    }
}
