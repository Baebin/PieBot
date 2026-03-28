package com.piebin.piebot.yacht.reaction;

import com.piebin.piebot.global.service.PieNonEmbedReactionAdd;
import com.piebin.piebot.global.utility.MessageRetriever;
import com.piebin.piebot.global.service.PieReactionAdd;
import com.piebin.piebot.yacht.service.YachtService;
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
