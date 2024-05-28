package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.impl.commands.MoneyRankCommand;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoneyRankReactionAdd implements PieReactionAdd {
    private final MoneyRankCommand moneyRankCommand;

    @Override
    public void execute(MessageReactionAddEvent event) {
        PageReactionAdd pageReactionAdd = new PageReactionAdd(Sentence.MONEY_RANK, moneyRankCommand);
        pageReactionAdd.execute(event);
    }
}
