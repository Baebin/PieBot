package com.piebin.piebot.global.service;

import com.piebin.piebot.global.entity.Sentence;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public interface PageReactionAdd {
    void execute(Sentence sentence, PageService pageService, MessageReactionAddEvent event);
}
