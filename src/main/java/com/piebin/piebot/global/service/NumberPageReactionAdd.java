package com.piebin.piebot.global.service;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public interface NumberPageReactionAdd {
    void execute(PageService pageService, MessageReactionAddEvent event);
}
