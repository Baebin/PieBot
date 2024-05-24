package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.entity.EmbedSentence;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.utility.DateTimeManager;
import com.piebin.piebot.utility.EmbedMessageHelper;
import com.piebin.piebot.utility.NumberManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

@Service
@Component
@RequiredArgsConstructor
public class ProfileCommand implements PieCommand {
    private final AccountRepository accountRepository;

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
        TextChannel channel = event.getChannel().asTextChannel();
        try {
            Account account = optional.get();
            InputStream in = new URL(event.getAuthor().getAvatarUrl()).openStream();
            FileUpload fileUpload = FileUpload.fromData(in, "profile.png");
            channel.sendFiles(fileUpload)
                    .setEmbeds(getProfile(account).build())
                    .queue();
        } catch (Exception e) {
            EmbedMessageHelper.printEmbedMessage(channel, EmbedSentence.PROFILE_NOT_FOUND, Color.RED);
        }
    }
}
