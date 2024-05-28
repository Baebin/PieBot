package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.dto.embed.EmbedDto;
import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.entity.DiscordEmoji;
import com.piebin.piebot.model.entity.ResultState;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.service.GamblingService;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.service.TaskSchedulerService;
import com.piebin.piebot.utility.CommandManager;
import com.piebin.piebot.utility.EmbedMessageHelper;
import com.piebin.piebot.utility.NumberManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.*;
import java.util.List;

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

    private String getMukchiba(int a) {
        if (a == 0)
            return "묵";
        if (a == 1)
            return "찌";
        return "빠";
    }

    private int getMukchiba(String a) {
        if (a.equals("묵"))
            return 0;
        if (a.equals("찌"))
            return 1;
        if (a.equals("빠"))
            return 2;
        return -1;
    }

    @Override
    @Transactional
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
                    if (args.size() < 5 || getMukchiba(args.get(4)) == -1) {
                        EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.GAMBLING_MUKCHIBA_ARG3);
                        return;
                    }
                    int me = getMukchiba(args.get(4));
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
                    embedBuilder.setColor((weight == 0 ? Color.RED : (weight < 1 ? Color.YELLOW : Color.GREEN)));

                    List<String> lines = new ArrayList<>();
                    lines.add(account.getName() + " vs " + "빙구 1 vs 빙구 2");
                    lines.add("");
                    lines.add(account.getName() + " : " + getMukchiba(me));
                    lines.add("빙구 1" + " : " + getMukchiba(o1));
                    lines.add("빙구 2" + " : " + getMukchiba(o2));
                    lines.add("");
                    lines.add("결과 : " + states[0].getResult() + states[1].getResult());
                    lines.add("적용 배율 : " + (weight == (long) weight ? (long) weight : weight) + "배");
                    lines.add("배팅 금액 : " + NumberManager.getNumber(money) + "빙");
                    lines.add("받은 금액 : " + NumberManager.getNumber(reward) + "빙");
                    lines.add("보유 자산 : " + NumberManager.getNumber(account.getMoney()) + "빙");

                    String description = String.join("\n", lines);
                    embedBuilder.appendDescription(description);

                    event.getMessage().replyEmbeds(embedBuilder.build()).queue();
                    return;
                }
            } catch (Exception e) {}
        }
        EmbedDto dto = new EmbedDto(CommandSentence.GAMBLING_MUKCHIBA_ARG2_MIN, Color.RED);
        dto.changeDescription(NumberManager.getNumber(account.getMoney()));
        EmbedMessageHelper.replyEmbedMessage(event.getMessage(), dto);
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

                    String randomFruits = DiscordEmoji.FRUIT_RANDOM.getEmoji() + DiscordEmoji.FRUIT_RANDOM.getEmoji() + DiscordEmoji.FRUIT_RANDOM.getEmoji();
                    String resultFruits = emojis[o1].getEmoji() + " " + emojis[o2].getEmoji() + " " + emojis[o3].getEmoji();

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
