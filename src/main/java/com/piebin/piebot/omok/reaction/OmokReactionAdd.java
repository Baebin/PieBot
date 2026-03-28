package com.piebin.piebot.omok.reaction;

import com.piebin.piebot.omok.service.OmokService;
import com.piebin.piebot.global.service.PieReactionAdd;
import com.piebin.piebot.global.utility.MessageRetriever;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OmokReactionAdd implements PieReactionAdd {
    private final MessageRetriever messageRetriever;
    private final OmokService omokService;

    @Override
    @Transactional
    public void execute(MessageReactionAddEvent event) {
        User user = event.getUser();
        MessageReaction reaction = event.getReaction();
        reaction.removeReaction(user).queue();

        messageRetriever.retrieveMessage(event, (message) -> omokService.createOmokRoom(event, message));
    }
}
