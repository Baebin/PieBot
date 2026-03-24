package com.piebin.piebot.service;

import com.piebin.piebot.model.entity.Sentence;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public interface PageReactionAdd {
    void execute(Sentence sentence, PageService pageService, MessageReactionAddEvent event);
}
