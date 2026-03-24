package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.service.NumberPageReactionAdd;
import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.impl.commands.EasterEggListCommand;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EasterEggListReactionAdd implements PieReactionAdd {
    private final NumberPageReactionAdd numberPageReactionAdd;
    private final EasterEggListCommand easterEggListCommand;

    @Override
    public void execute(MessageReactionAddEvent event) {
        numberPageReactionAdd.execute(easterEggListCommand, event);
    }
}
