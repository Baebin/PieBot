package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.impl.commands.HelpCommand;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelpReactionAdd implements PieReactionAdd {
    private final HelpCommand helpCommand;

    @Override
    public void execute(MessageReactionAddEvent event) {
        NumberPageReactionAdd numberPageReactionAdd = new NumberPageReactionAdd(helpCommand);
        numberPageReactionAdd.execute(event);
    }
}
