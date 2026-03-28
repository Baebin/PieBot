package com.piebin.piebot.global.service.impl.commands;

import com.piebin.piebot.global.domain.Account;
import com.piebin.piebot.global.entity.Sentence;
import com.piebin.piebot.global.repository.AccountRepository;
import com.piebin.piebot.global.entity.EmbedSentence;
import com.piebin.piebot.global.service.PieCommand;
import com.piebin.piebot.global.utility.DateTimeManager;
import com.piebin.piebot.global.utility.EmbedMessageHelper;
import com.piebin.piebot.global.utility.MessageManager;
import com.piebin.piebot.global.utility.NumberManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileCommand implements PieCommand {
    private final AccountRepository accountRepository;

    private final EmbedMessageHelper embedMessageHelper;

    private EmbedBuilder getProfile(Account account) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Sentence.PROFILE.getMessage());
        embedBuilder.setColor(Color.GREEN);

        embedBuilder.addField("이름", account.getName(), false);
        embedBuilder.addField("자산", NumberManager.getNumber(account.getMoney()) + "빙", false);
        embedBuilder.addField("가입일", DateTimeManager.getDate(account.getRegDate()), false);

        return embedBuilder;
    }

    @Override
    @Transactional(readOnly = true)
    public void execute(MessageReceivedEvent event) {
        String userId = event.getAuthor().getId();
        Optional<Account> optional = accountRepository.findById(userId);
        if (optional.isEmpty())
            return;
        FileUpload fileUpload = MessageManager.getProfile(event.getAuthor().getAvatarUrl());
        if (fileUpload != null) {
            Account account = optional.get();
            event.getMessage().replyFiles(fileUpload)
                    .setEmbeds(getProfile(account).build())
                    .queue();
        } else embedMessageHelper.replyEmbedMessage(event.getMessage(), EmbedSentence.PROFILE_NOT_FOUND, Color.RED);
    }
}
