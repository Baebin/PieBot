package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.service.PieNonEmbedReactionAdd;
import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.YachtService;
import com.piebin.piebot.utility.MessageRetriever;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class YachtReactionAdd implements PieReactionAdd, PieNonEmbedReactionAdd {
    private final YachtService yachtService;

    private final MessageRetriever messageRetriever;

    @Override
    @Transactional
    public void execute(MessageReactionAddEvent event) {
        messageRetriever.retrieveMessage(event, message -> yachtService.createYachtRoom(event, message));
    }

    @Override
    @Transactional
    public void executeWithNonEmbed(MessageReactionAddEvent event) {
        yachtService.selectEmoji(event);
    }
}
