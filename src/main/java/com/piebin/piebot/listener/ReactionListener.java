package com.piebin.piebot.listener;

import com.piebin.piebot.service.ReactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Slf4j
@RequiredArgsConstructor
public class ReactionListener extends ListenerAdapter {
    private final ReactionService reactionService;

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
        reactionService.run(event);
    }
}