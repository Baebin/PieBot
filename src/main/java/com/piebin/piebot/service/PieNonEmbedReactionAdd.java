package com.piebin.piebot.service;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public interface PieNonEmbedReactionAdd {
    void executeWithNonEmbed(MessageReactionAddEvent event);
}
