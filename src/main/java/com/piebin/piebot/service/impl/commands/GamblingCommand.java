package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.dto.embed.EmbedDto;
import com.piebin.piebot.model.entity.*;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.service.GamblingService;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.service.TaskSchedulerService;
import com.piebin.piebot.utility.CommandManager;
import com.piebin.piebot.utility.EmbedMessageHelper;
import com.piebin.piebot.utility.NumberManager;
import com.piebin.piebot.utility.ReactionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.*;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GamblingCommand implements PieCommand, GamblingService {
    private final AccountRepository accountRepository;

    private final TaskSchedulerService taskSchedulerService;

    @Override
    @Transactional
    public void execute(MessageReceivedEvent event) {
        List<String> args = CommandManager.getArgs(event);
        if (args.size() >= 3) {
            if (args.get(2).equals("묵찌빠")) {
                runMukchiba(event);
                return;
            }
            if (args.get(2).equals("슬롯머신")) {
                runSlotMachine(event);
                return;
            }
        }
        EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.GAMBLING_ARG1);
    }

    @Override
    @Transactional(readOnly = true)
    public void runMukchiba(MessageReceivedEvent event) {
        Optional<Account> optional = accountRepository.findById(event.getAuthor().getId());
        if (optional.isEmpty())
            return;
        Account account = optional.get();
        List<String> args = CommandManager.getArgs(event);
        if (args.size() >= 4) {
            try {
                long money = Long.parseLong(args.get(3));
                if (1 <= money) {
                    if (account.getMoney() < money) {
                        EmbedDto dto = new EmbedDto(CommandSentence.GAMBLING_MUKCHIBA_ARG2_LESS, Color.RED);
                        dto.changeDescription(NumberManager.getNumber(account.getMoney()));
                        EmbedMessageHelper.replyEmbedMessage(event.getMessage(), dto);
                        return;
                    }
                    EmbedDto dto = new EmbedDto(CommandSentence.GAMBLING_MUKCHIBA_RUN, Color.GREEN);
                    dto.changeMessage(NumberManager.getNumber(money));
                    Message message = EmbedMessageHelper.replyEmbedMessage(event.getMessage(), dto);

                    message.addReaction(DiscordEmoji.MUKCHIBA_ROCK.getEmoji()).queue();
                    message.addReaction(DiscordEmoji.MUKCHIBA_SCISSORS.getEmoji()).queue();
                    message.addReaction(DiscordEmoji.MUKCHIBA_PAPER.getEmoji()).queue();
                    return;
                }
            } catch (Exception e) {}
        }
        EmbedDto dto = new EmbedDto(CommandSentence.GAMBLING_MUKCHIBA_ARG2_MIN, Color.RED);
        dto.changeDescription(NumberManager.getNumber(account.getMoney()));
        EmbedMessageHelper.replyEmbedMessage(event.getMessage(), dto);
    }

    // 묵(0), 찌(1), 빠(2)
    private ResultState getMukchibaResult(int a, int b) {
        if (a == b)
            return ResultState.TIE;
        if (a == 0 && b == 1)
            return ResultState.WIN;
        if (a == 1 && b == 2)
            return ResultState.WIN;
        if (a == 2 && b == 0)
            return ResultState.WIN;
        return ResultState.LOSE;
    }

    private DiscordEmoji getMukchiba(int a) {
        if (a == 0)
            return DiscordEmoji.MUKCHIBA_ROCK;
        if (a == 1)
            return DiscordEmoji.MUKCHIBA_SCISSORS;
        return DiscordEmoji.MUKCHIBA_PAPER;
    }

    private int getMukchiba(Emoji emoji) {
        if (DiscordEmoji.MUKCHIBA_ROCK.equals(emoji))
            return 0;
        if (DiscordEmoji.MUKCHIBA_SCISSORS.equals(emoji))
            return 1;
        if (DiscordEmoji.MUKCHIBA_PAPER.equals(emoji))
            return 2;
        return -1;
    }

    @Override
    @Transactional
    public void runMukchiba(MessageReactionAddEvent event) {
        // Emoji Check
        int me = getMukchiba(event.getReaction().getEmoji());
        if (me == -1)
            return;
        event.getReaction().removeReaction(event.getUser()).queue();

        // Message Check
        Message message = ReactionManager.getMessage(event);
        if (message == null)
            return;
        Message rMessage = message.getReferencedMessage();
        if (rMessage == null || !rMessage.getAuthor().getId().equals(event.getUserId()))
            return;
        // Command Check
        MessageEmbed embed = ReactionManager.getEmbed(message);
        if (embed.getFields().size() < 1)
            return;
        String name = embed.getFields().get(0).getName();
        if (!name.contains("배팅"))
            return;
        // Money Check
        long money;
        try {
            String strMoney = name.replace("배팅 금액: ", "").replace("빙", "").replace(",", "");
            money = Long.parseLong(strMoney);
        } catch (Exception e) {
            return;
        }
        Optional<Account> optional = accountRepository.findById(event.getUserId());
        if (optional.isEmpty())
            return;
        Account account = optional.get();
        if (account.getMoney() < money) {
            EmbedBuilder embedBuilder = EmbedMessageHelper.getEmbedBuilder(CommandSentence.GAMBLING_MUKCHIBA_ARG3, Color.RED);
            message.editMessageEmbeds(embedBuilder.build()).queue();
            return;
        }
        // Main Logic
        int o1 = new Random().nextInt(3);
        int o2 = new Random().nextInt(3);

        ResultState[] states = { getMukchibaResult(me, o1), getMukchibaResult(me, o2) };

        double weight;
        int result = (states[0].getWeight() + states[1].getWeight());
        if (result < 0)
            weight = 0;
        else if (result == 0)
            weight = 0.9;
        else if (result == 1)
            weight = 2;
        else weight = 3;

        long reward = (int)(money * weight);
        account.setMoney(account.getMoney() + reward - money);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Sentence.GAMBLING_MUKCHIBA.getMessage());
        embedBuilder.setColor(Color.GRAY);

        List<String> preLines = new ArrayList<>();
        preLines.add(account.getName() + " vs " + "빙구 vs 구빙");
        preLines.add("");
        preLines.add("유저" + " : " + DiscordEmoji.MUKCHIBA_RANDOM);
        preLines.add("빙구" + " : " + DiscordEmoji.MUKCHIBA_RANDOM);
        preLines.add("구빙" + " : " + DiscordEmoji.MUKCHIBA_RANDOM);

        String preDescription = String.join("\n", preLines);
        embedBuilder.setDescription(preDescription);

        message.editMessageEmbeds(embedBuilder.build()).queue();

        taskSchedulerService.runAfterSeconds(
                () -> {
                    embedBuilder.setColor((weight == 0 ? Color.RED : (weight < 1 ? Color.YELLOW : Color.GREEN)));

                    List<String> lines = new ArrayList<>();
                    lines.add(account.getName() + " vs " + "빙구 vs 구빙");
                    lines.add("");
                    lines.add("유저" + " : " + getMukchiba(me));
                    lines.add("빙구" + " : " + getMukchiba(o1));
                    lines.add("구빙" + " : " + getMukchiba(o2));
                    lines.add("");
                    lines.add("결과 : " + states[0].getResult() + states[1].getResult());
                    lines.add("적용 배율 : " + NumberManager.parseString(weight) + "배");
                    lines.add("배팅 금액 : " + NumberManager.getNumber(money) + "빙");
                    lines.add("받은 금액 : " + NumberManager.getNumber(reward) + "빙");
                    lines.add("보유 자산 : " + NumberManager.getNumber(account.getMoney()) + "빙");

                    String description = String.join("\n", lines);
                    embedBuilder.setDescription(description);

                    message.editMessageEmbeds(embedBuilder.build()).queue();
                },
                2
        );
    }

    @Override
    @Transactional
    public void runSlotMachine(MessageReceivedEvent event) {
        Optional<Account> optional = accountRepository.findById(event.getAuthor().getId());
        if (optional.isEmpty())
            return;
        Account account = optional.get();

        List<String> args = CommandManager.getArgs(event);
        if (args.size() >= 4) {
            try {
                long money = Long.parseLong(args.get(3));
                if (1 <= money) {
                    if (account.getMoney() < money) {
                        EmbedDto dto = new EmbedDto(CommandSentence.GAMBLING_MUKCHIBA_ARG2_MIN, Color.RED);
                        dto.changeDescription(NumberManager.getNumber(account.getMoney()));
                        EmbedMessageHelper.replyEmbedMessage(event.getMessage(), dto);
                        return;
                    }
                    DiscordEmoji[] emojis = {
                            DiscordEmoji.FRUIT_COCONUT,
                            DiscordEmoji.FRUIT_APPLE, DiscordEmoji.FRUIT_MANGO,
                            DiscordEmoji.FRUIT_PLUM, DiscordEmoji.FRUIT_PITCH
                    };
                    int o1 = new Random().nextInt(5);
                    int o2 = new Random().nextInt(5);
                    int o3 = new Random().nextInt(5);

                    int cnt, weight;
                    if (o1 == o2 && o2 == o3) {
                        cnt = 3;
                        if (o1 == 0)
                            weight = 7;
                        else weight = 5;
                    } else if (o1 == o2 || o1 == o3 || o2 == o3) {
                        int idx;
                        if (o1 == o2)
                            idx = o1;
                        else if (o1 == o3)
                            idx = o1;
                        else idx = o2;

                        cnt = 2;
                        // Star
                        if (idx == 0)
                            weight = 3;
                        else weight = 2;
                    } else {
                        cnt = 1;
                        weight = 0;
                    }
                    long reward = (money * weight);
                    account.setMoney(account.getMoney() + reward - money);

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setTitle(Sentence.GAMBLING_SLOTMACHINE.getMessage());
                    embedBuilder.setDescription("추첨중 ....");
                    embedBuilder.setColor(Color.GRAY);

                    String randomFruits = "" + DiscordEmoji.FRUIT_RANDOM + DiscordEmoji.FRUIT_RANDOM + DiscordEmoji.FRUIT_RANDOM;
                    String resultFruits = emojis[o1] + " " + emojis[o2] + " " + emojis[o3];

                    Message mainMsg = event.getMessage().replyEmbeds(embedBuilder.build()).complete();
                    Message subMsg = event.getChannel().sendMessage(randomFruits).complete();

                    taskSchedulerService.runAfterSeconds(
                            () -> {
                                embedBuilder.setColor((weight == 0 ? Color.RED : (weight < 1 ? Color.YELLOW : Color.GREEN)));

                                List<String> lines = new ArrayList<>();
                                lines.add("결과 : " + (cnt == 1 ? "실패" : cnt + "개 성공"));
                                lines.add("적용 배율 : " + weight + "배");
                                lines.add("배팅 금액 : " + NumberManager.getNumber(money) + "빙");
                                lines.add("받은 금액 : " + NumberManager.getNumber(reward) + "빙");
                                lines.add("보유 자산 : " + NumberManager.getNumber(account.getMoney()) + "빙");

                                String description = String.join("\n", lines);
                                embedBuilder.setDescription(description);

                                subMsg.editMessage(resultFruits).queue();
                                mainMsg.editMessageEmbeds(embedBuilder.build()).queue();
                            },
                            2
                    );
                    return;
                }
            } catch (Exception e) {}
        }
        EmbedDto dto = new EmbedDto(CommandSentence.GAMBLING_SLOTMACHINE_ARG2_MIN, Color.RED);
        dto.changeDescription(NumberManager.getNumber(account.getMoney()));
        EmbedMessageHelper.replyEmbedMessage(event.getMessage(), dto);
    }
}
