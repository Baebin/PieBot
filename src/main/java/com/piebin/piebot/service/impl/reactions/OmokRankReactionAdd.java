package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.impl.commands.OmokRankCommand;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OmokRankReactionAdd implements PieReactionAdd {
    private final OmokRankCommand omokRankCommand;

    @Override
    public void execute(MessageReactionAddEvent event) {
        PageReactionAdd pageReactionAdd = new PageReactionAdd(Sentence.OMOK_RANK, omokRankCommand);
        pageReactionAdd.execute(event);
    }
}
