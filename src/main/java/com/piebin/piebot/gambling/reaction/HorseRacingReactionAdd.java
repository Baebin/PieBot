package com.piebin.piebot.gambling.reaction;

import com.piebin.piebot.gambling.command.GamblingCommand;
import com.piebin.piebot.global.utility.MessageRetriever;
import com.piebin.piebot.global.service.PieReactionAdd;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HorseRacingReactionAdd implements PieReactionAdd {
    private final GamblingCommand gamblingCommand;

    private final MessageRetriever messageRetriever;

    @Override
    public void execute(MessageReactionAddEvent event) {
        messageRetriever.retrieveMessage(event, message -> gamblingCommand.runHorseRacing(event, message));
    }
}
