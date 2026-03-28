package com.piebin.piebot.global.service.impl.reactions;

import com.piebin.piebot.global.entity.Sentence;
import com.piebin.piebot.global.service.PageReactionAdd;
import com.piebin.piebot.global.service.PieReactionAdd;
import com.piebin.piebot.global.service.impl.commands.MoneyRankCommand;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoneyRankReactionAdd implements PieReactionAdd {
    private final PageReactionAdd pageReactionAdd;
    private final MoneyRankCommand moneyRankCommand;

    @Override
    public void execute(MessageReactionAddEvent event) {
        pageReactionAdd.execute(Sentence.MONEY_RANK, moneyRankCommand, event);
    }
}
