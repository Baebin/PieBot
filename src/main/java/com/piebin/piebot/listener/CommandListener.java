package com.piebin.piebot.listener;

import com.piebin.piebot.model.entity.CommandParameter;
import com.piebin.piebot.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CommandListener extends ListenerAdapter {
    private static final String PREFIX = "ㅋ";

    private final CommandService commandService;

    private boolean checkArg(String arg, CommandParameter commandParameter) {
        for (String data : commandParameter.getData())
            if (arg.equalsIgnoreCase(data))
                return true;
        return false;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);

        User user = event.getAuthor();
        if (user.isBot())
            return;
        Message message = event.getMessage();

        List<String> args = Arrays.asList(message.getContentRaw().split(" "));
        log.info("user: {}, args: {}", user, args);

        if (args.get(0).equals(PREFIX)) {
            if (args.size() == 1)
                return;
            if (checkArg(args.get(1), CommandParameter.TEST)) {
                commandService.sendBabo(event);
            }
        }
    }
}
