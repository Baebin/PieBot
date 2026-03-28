package com.piebin.piebot.global.service.impl;

import com.piebin.piebot.gambling.command.GamblingCommand;
import com.piebin.piebot.global.entity.*;
import com.piebin.piebot.global.factory.CommandFactory;
import com.piebin.piebot.global.domain.Account;
import com.piebin.piebot.global.repository.AccountRepository;
import com.piebin.piebot.global.repository.EasterEggHistoryRepository;
import com.piebin.piebot.global.repository.EasterEggWordRepository;
import com.piebin.piebot.global.service.CommandService;
import com.piebin.piebot.global.service.impl.commands.*;
import com.piebin.piebot.global.domain.EasterEgg;
import com.piebin.piebot.global.domain.EasterEggHistory;
import com.piebin.piebot.global.domain.EasterEggWord;
import com.piebin.piebot.global.dto.embed.EmbedDto;
import com.piebin.piebot.omok.command.OmokCommand;
import com.piebin.piebot.global.service.PieCommand;
import com.piebin.piebot.yacht.service.YachtService;
import com.piebin.piebot.global.utility.CommandManager;
import com.piebin.piebot.global.utility.EmbedMessageHelper;
import com.piebin.piebot.global.utility.NumberManager;
import com.piebin.piebot.yacht.command.YachtCommand;
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

    private final YachtService yachtService;

    private final AccountRepository accountRepository;

    private final EasterEggWordRepository easterEggWordRepository;
    private final EasterEggHistoryRepository easterEggHistoryRepository;

    private final ProfileCommand profileCommand;
    private final PatchNoteCommand patchNoteCommand;
    private final PayCommand payCommand;
    private final RewardCommand rewardCommand;
    private final AttendanceCommand attendanceCommand;
    private final AttendanceRankCommand attendanceRankCommand;
    private final OmokCommand omokCommand;
    private final YachtCommand yachtCommand;
    private final GamblingCommand gamblingCommand;
    private final InventoryCommand inventoryCommand;
    private final ShopCommand shopCommand;
    private final ContributorCommand contributorCommand;
    private final EasterEggCommand easterEggCommand;
    private final EasterEggListCommand easterEggListCommand;

    private final TestCommand testCommand;

    private final CommandFactory commandFactory;
    
    private final EmbedMessageHelper embedMessageHelper;

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

            // Yacht
            if (args.size() == 2)
                yachtService.select(event, args.get(1));

            // Omok
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
                embedMessageHelper.printEmbedMessage(channel, easterEgg.getTitle(), easterEgg.getMessage(), easterEgg.getIdx() + Sentence.IS_EASTER_EGG.getMessage(), Color.GREEN);
                recordEasterEgg(user.getId(), easterEgg, event.getMessage());
                return;
            }
            for (CommandParameter parameter : CommandParameter.values()) {
                if (!checkArg(args.get(1), parameter))
                    continue;
                if (!accountRepository.existsById(user.getId())) {
                    embedMessageHelper.replyEmbedMessage(event.getMessage(), EmbedSentence.REGISTER, Color.GREEN, (embed) -> {
                        embed.addReaction(UniEmoji.CHECK.getEmoji()).queue();
                    });
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
                else if (parameter == CommandParameter.ATTENDANCE_RANK)
                    attendanceRankCommand.execute(event);
                else if (parameter == CommandParameter.OMOK_PVP
                        || parameter == CommandParameter.OMOK_QUIT
                        || parameter == CommandParameter.OMOK_PROFILE
                        || parameter == CommandParameter.OMOK_CONTINUE
                        || parameter == CommandParameter.OMOK_SKIN)
                    omokCommand.execute(event);
                else if (parameter == CommandParameter.YACHT_PVP
                        || parameter == CommandParameter.YACHT_QUIT
                        || parameter == CommandParameter.YACHT_CONTINUE)
                    yachtCommand.execute(event);
                else if (parameter == CommandParameter.GAMBLING_MUKCHIBA
                        || parameter == CommandParameter.GAMBLING_SLOTMACHINE
                        || parameter == CommandParameter.GAMBLING_HORSE_RACING)
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
                else {
                    PieCommand pieCommand = commandFactory.getCommand(parameter);
                    if (parameter != null)
                        pieCommand.execute(event);
                }
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
            embedMessageHelper.replyEmbedMessage(message, dto);
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
            embedMessageHelper.replyEmbedMessage(message, dto);
        }
    }
}
