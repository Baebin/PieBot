package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.impl.commands.ShopCommand;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopReactionAdd implements PieReactionAdd {
    private final ShopCommand shopCommand;

    @Override
    public void execute(MessageReactionAddEvent event) {
        NumberPageReactionAdd numberPageReactionAdd = new NumberPageReactionAdd(shopCommand);
        numberPageReactionAdd.execute(event);
    }
}
