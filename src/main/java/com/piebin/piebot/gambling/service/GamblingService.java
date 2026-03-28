package com.piebin.piebot.gambling.service;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public interface GamblingService {
    void runMukchiba(MessageReceivedEvent event);
    void runMukchiba(MessageReactionAddEvent event, Message message);
    void runSlotMachine(MessageReceivedEvent event);
    void runHorseRacing(MessageReceivedEvent event);
    void runHorseRacing(MessageReactionAddEvent event, Message message);
}
