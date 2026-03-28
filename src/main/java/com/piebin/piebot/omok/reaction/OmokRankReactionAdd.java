package com.piebin.piebot.omok.reaction;

import com.piebin.piebot.global.entity.Sentence;
import com.piebin.piebot.global.service.PageReactionAdd;
import com.piebin.piebot.global.service.PieReactionAdd;
import com.piebin.piebot.omok.command.OmokRankCommand;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OmokRankReactionAdd implements PieReactionAdd {
    private final PageReactionAdd pageReactionAdd;
    private final OmokRankCommand omokRankCommand;

    @Override
    public void execute(MessageReactionAddEvent event) {
        pageReactionAdd.execute(Sentence.OMOK_RANK, omokRankCommand, event);
    }
}
