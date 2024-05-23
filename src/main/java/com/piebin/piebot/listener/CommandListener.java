package com.piebin.piebot.listener;

import com.piebin.piebot.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Slf4j
@RequiredArgsConstructor
public class CommandListener extends ListenerAdapter {
    private final CommandService commandService;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);

        commandService.runCommand(event);
    }
}
