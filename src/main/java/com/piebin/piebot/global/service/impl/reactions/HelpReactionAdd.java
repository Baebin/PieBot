package com.piebin.piebot.global.service.impl.reactions;

import com.piebin.piebot.global.service.impl.commands.HelpCommand;
import com.piebin.piebot.global.service.NumberPageReactionAdd;
import com.piebin.piebot.global.service.PieReactionAdd;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelpReactionAdd implements PieReactionAdd {
    private final NumberPageReactionAdd numberPageReactionAdd;
    private final HelpCommand helpCommand;

    @Override
    public void execute(MessageReactionAddEvent event) {
        numberPageReactionAdd.execute(helpCommand, event);
    }
}
