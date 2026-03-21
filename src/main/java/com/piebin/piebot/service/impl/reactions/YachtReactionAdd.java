package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.YachtService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class YachtReactionAdd implements PieReactionAdd {
    private final YachtService yachtService;

    @Override
    @Transactional
    public void execute(MessageReactionAddEvent event) {
        yachtService.createYachtRoom(event);
    }
}
