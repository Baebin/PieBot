package com.piebin.piebot.service;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public interface PieReactionAdd {
    void execute(MessageReactionAddEvent event);
}
