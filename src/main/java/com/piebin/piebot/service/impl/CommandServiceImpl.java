package com.piebin.piebot.service.impl;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.EasterEgg;
import com.piebin.piebot.model.domain.EasterEggHistory;
import com.piebin.piebot.model.domain.EasterEggWord;
import com.piebin.piebot.model.dto.embed.EmbedDto;
import com.piebin.piebot.model.entity.*;
import com.piebin.piebot.model.repository.*;
import com.piebin.piebot.service.CommandService;
import com.piebin.piebot.service.impl.commands.*;
import com.piebin.piebot.utility.CommandManager;
import com.piebin.piebot.utility.EmbedMessageHelper;
import com.piebin.piebot.utility.NumberManager;
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
    private final PatchNoteCommand patchNoteCommand;
    private final PayCommand payCommand;
    private final RewardCommand rewardCommand;
    private final AttendanceCommand attendanceCommand;
    private final OmokCommand omokCommand;
    private final GamblingCommand gamblingCommand;
    private final InventoryCommand inventoryCommand;
    private final ShopCommand shopCommand;
    private final ContributorCommand contributorCommand;
    private final EasterEggCommand easterEggCommand;
    private final EasterEggListCommand easterEggListCommand;

    private final TestCommand testCommand;

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
        if (args.get(0).equals(PREFIX) || args.get(0).equalsIgnoreCase(PREFIX_ENGLISH)) {
            if (args.size() == 1)
                return;
            log.info("user: {}, args: {}", user, args);
            TextChannel channel = event.getChannel().asTextChannel();

            if (args.get(1).length() == 2 || args.get(1).length() == 3) {
                try {
                    char x = args.get(1).toUpperCase().charAt(0);
                    int y = Integer.parseInt(args.get(1).substring(1));
                    if (('A' <= x && x <= 'A' + (OmokCommand.MAXIMUM_SIZE - 1)) && (1 <= y && y <= OmokCommand.MAXIMUM_SIZE)) {
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
                else if (parameter == CommandParameter.PATCH_NOTE)
                    patchNoteCommand.execute(event);
                else if (parameter == CommandParameter.PAY)
                    payCommand.execute(event);
                else if (parameter == CommandParameter.REWARD)
                    rewardCommand.execute(event);
                else if (parameter == CommandParameter.ATTENDANCE)
                    attendanceCommand.execute(event);
                else if (parameter == CommandParameter.OMOK_PVP
                        || parameter == CommandParameter.OMOK_QUIT
                        || parameter == CommandParameter.OMOK_PROFILE
                        || parameter == CommandParameter.OMOK_CONTINUE
                        || parameter == CommandParameter.OMOK_SKIN)
                    omokCommand.execute(event);
                else if (parameter == CommandParameter.GAMBLING_MUKCHIBA
                        || parameter == CommandParameter.GAMBLING_SLOTMACHINE)
                    gamblingCommand.execute(event);
                else if (parameter == CommandParameter.INVENTORY)
                    inventoryCommand.execute(event);
                else if (parameter == CommandParameter.SHOP_LIST
                        || parameter == CommandParameter.SHOP_INFO
                        || parameter == CommandParameter.SHOP_BUY)
                    shopCommand.execute(event);
                else if (parameter == CommandParameter.CONTRIBUTOR)
                    contributorCommand.execute(event);
                else if (parameter == CommandParameter.SECRET_EASTEREGG)
                    easterEggCommand.execute(event);
                else if (parameter == CommandParameter.SECRET_EASTEREGG_LIST)
                    easterEggListCommand.execute(event);
                else if (parameter == CommandParameter.SECRET_TEST)
                    testCommand.execute(event);
                else parameter.getCommand().execute(event);
                break;
            }
        }
    }

    @Override
    @Transactional
    public void recordEasterEgg(String id, EasterEgg easterEgg, Message message) {
        Optional<Account> optional = accountRepository.findById(id);
        if (optional.isEmpty())
            return;
        Account account = optional.get();
        if (easterEggHistoryRepository.existsByEasterEgg(easterEgg)) {
            if (easterEggHistoryRepository.existsByAccountAndEasterEgg(account, easterEgg))
                return;
            // [2'000, 5%]
            long reward = Math.max(2000, (account.getMoney() * 5 / 100));
            account.setMoney(account.getMoney() + reward);

            EasterEggHistory easterEggHistory = EasterEggHistory.builder()
                    .account(account)
                    .easterEgg(easterEgg)
                    .isFirst(false)
                    .build();
            easterEggHistoryRepository.save(easterEggHistory);

            EmbedDto dto = new EmbedDto(EmbedSentence.EASTER_EGG_FIND_ALREADY, Color.CYAN);
            dto.changeMessage(NumberManager.getNumber(reward));
            EmbedMessageHelper.replyEmbedMessage(message, dto);
        } else {
            // [10'000, 25%]
            long reward = Math.max(10000, (account.getMoney() * 25 / 100));
            account.setMoney(account.getMoney() + reward);

            EasterEggHistory easterEggHistory = EasterEggHistory.builder()
                    .account(account)
                    .easterEgg(easterEgg)
                    .isFirst(true)
                    .build();
            easterEggHistoryRepository.save(easterEggHistory);

            EmbedDto dto = new EmbedDto(EmbedSentence.EASTER_EGG_FIND, Color.CYAN);
            dto.changeMessage(NumberManager.getNumber(reward));
            EmbedMessageHelper.replyEmbedMessage(message, dto);
        }
    }
}
