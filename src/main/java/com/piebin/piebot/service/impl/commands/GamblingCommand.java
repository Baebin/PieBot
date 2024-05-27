package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.dto.embed.EmbedDto;
import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.entity.ResultState;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.service.GamblingService;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.utility.CommandManager;
import com.piebin.piebot.utility.EmbedMessageHelper;
import com.piebin.piebot.utility.NumberManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GamblingCommand implements PieCommand, GamblingService {
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void execute(MessageReceivedEvent event) {
        List<String> args = CommandManager.getArgs(event);
        if (args.size() >= 3) {
            if (args.get(2).equals("묵찌빠")) {
                runMukchiba(event);
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
                    lines.add("결과: " + states[0].getResult() + states[1].getResult());
                    lines.add("적용 배율: " + (weight == (long) weight ? (long) weight : weight) + "배");
                    lines.add("배팅 금액: " + NumberManager.getNumber(money) + "빙");
                    lines.add("받은 금액: " + NumberManager.getNumber(reward) + "빙");
                    lines.add("보유 자산: " + NumberManager.getNumber(account.getMoney()) + "빙");

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
}
