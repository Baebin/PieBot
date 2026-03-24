package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.impl.commands.GamblingCommand;
import com.piebin.piebot.utility.impl.MessageRetrieverImpl;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MukchibaReactionAdd implements PieReactionAdd {
    private final GamblingCommand gamblingCommand;

    private final MessageRetrieverImpl messageRetriever;

    @Override
    public void execute(MessageReactionAddEvent event) {
        messageRetriever.retrieveMessage(event, (message) -> gamblingCommand.runMukchiba(event, message));
    }
}
