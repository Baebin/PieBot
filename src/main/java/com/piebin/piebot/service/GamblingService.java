package com.piebin.piebot.service;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public interface GamblingService {
    void runMukchiba(MessageReceivedEvent event);
    void runMukchiba(MessageReactionAddEvent event);
    void runSlotMachine(MessageReceivedEvent event);
}
