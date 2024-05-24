package com.piebin.piebot.service;

import com.piebin.piebot.model.domain.EasterEgg;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface CommandService {
    void run(MessageReceivedEvent event);
    void recordEasterEgg(String id, EasterEgg easterEgg, Message message);
}
