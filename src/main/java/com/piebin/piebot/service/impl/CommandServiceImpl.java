package com.piebin.piebot.service.impl;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.EasterEgg;
import com.piebin.piebot.model.domain.EasterEggHistory;
import com.piebin.piebot.model.domain.EasterEggWord;
import com.piebin.piebot.model.entity.*;
import com.piebin.piebot.model.repository.*;
import com.piebin.piebot.service.CommandService;
import com.piebin.piebot.service.impl.commands.*;
import com.piebin.piebot.utility.CommandManager;
import com.piebin.piebot.utility.EmbedMessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {
    public static final String PREFIX = "ㅋ";
    public static final String PREFIX_ENGLISH = "z";

    private final AccountRepository accountRepository;

    private final EasterEggWordRepository easterEggWordRepository;
    private final EasterEggHistoryRepository easterEggHistoryRepository;

    private final ProfileCommand profileCommand;
    private final PayCommand payCommand;
    private final OmokCommand omokCommand;
    private final EasterEggCommand easterEggCommand;
    private final EasterEggListCommand easterEggListCommand;

    private boolean checkArg(String arg, CommandParameter commandParameter) {
        for (String data : commandParameter.getData()) {
            if (
                    (commandParameter.getMode() == CommandMode.EQUAL && arg.equalsIgnoreCase(data))
                    || (commandParameter.getMode() == CommandMode.CONTAIN && arg.toLowerCase().contains(data))
            ) return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void run(MessageReceivedEvent event) {
        User user = event.getAuthor();
        if (user.isBot())
            return;
        List<String> args = CommandManager.getArgs(event);
        log.info("user: {}, args: {}", user, args);

        if (args.get(0).equals(PREFIX) || args.get(0).equalsIgnoreCase(PREFIX_ENGLISH)) {
            if (args.size() == 1)
                return;
            TextChannel channel = event.getChannel().asTextChannel();

            if (args.get(1).length() == 2 || args.get(1).length() == 3) {
                try {
                    char x = args.get(1).toUpperCase().charAt(0);
                    int y = Integer.parseInt(args.get(1).substring(1, args.get(1).length()));
                    if (('A' <= x && x <= 'A' + 13) && (1 <= y && y <= 14)) {
                        Optional<Account> optionalAccount = accountRepository.findById(user.getId());
                        if (optionalAccount.isEmpty())
                            return;
                        omokCommand.selectPosition(event, optionalAccount.get(), x, y);
                    }
                } catch (Exception e) {}
            }

            // Easter Egg
            List<EasterEggWord> words = easterEggWordRepository.findByWordIgnoreCase(args.get(1));
            if (!words.isEmpty()) {
                EasterEgg easterEgg = words.get(0).getEasterEgg();
                EmbedMessageHelper.printEmbedMessage(channel, easterEgg.getTitle(), easterEgg.getMessage(), easterEgg.getIdx() + Sentence.IS_EASTER_EGG.getMessage(), Color.GREEN);
                recordEasterEgg(user.getId(), easterEgg, event.getMessage());
                return;
            }
            for (CommandParameter parameter : CommandParameter.values()) {
                if (!checkArg(args.get(1), parameter))
                    continue;
                if (!accountRepository.existsById(user.getId())) {
                    Message message = EmbedMessageHelper.replyEmbedMessage(event.getMessage(), EmbedSentence.REGISTER, Color.GREEN);
                    message.addReaction(UniEmoji.CHECK.getEmoji()).queue();
                    return;
                }
                if (parameter == CommandParameter.PROFILE)
                    profileCommand.execute(event);
                else if (parameter == CommandParameter.PAY)
                    payCommand.execute(event);
                else if (parameter == CommandParameter.OMOK_PVP
                        || parameter == CommandParameter.OMOK_QUIT
                        || parameter == CommandParameter.OMOK_PROFILE
                        || parameter == CommandParameter.OMOK_CONTINUE)
                    omokCommand.execute(event);
                else if (parameter == CommandParameter.SECRET_EASTEREGG)
                    easterEggCommand.execute(event);
                else if (parameter == CommandParameter.SECRET_EASTEREGG_LIST)
                    easterEggListCommand.execute(event);
                else parameter.getCommand().execute(event);
                break;
            }
        }
    }

    @Override
    @Transactional
    public void recordEasterEgg(String id, EasterEgg easterEgg, Message message) {
        if (easterEggHistoryRepository.existsByEasterEgg(easterEgg))
            return;
        Optional<Account> optional = accountRepository.findById(id);
        if (optional.isEmpty())
            return;
        Account account = optional.get();
        account.setMoney(account.getMoney() + 3000);
        EasterEggHistory easterEggHistory = EasterEggHistory.builder()
                .account(account)
                .easterEgg(easterEgg)
                .build();
        easterEggHistoryRepository.save(easterEggHistory);

        message.replyEmbeds(EmbedMessageHelper.getEmbedBuilder(EmbedSentence.EASTER_EGG_FIND, Color.CYAN).build()).queue();
    }
}
