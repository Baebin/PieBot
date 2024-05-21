package com.piebin.piebot.service;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface CommandService {
    void sendBabo(MessageReceivedEvent event);
}
