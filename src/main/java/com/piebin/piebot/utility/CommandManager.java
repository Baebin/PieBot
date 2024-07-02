package com.piebin.piebot.utility;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class CommandManager {
    public static List<String> getArgs(MessageReceivedEvent event) {
        // Remove Consecutive Spaces
        String message = event.getMessage().getContentRaw()
                .replaceAll("\\s+", " ");
        return Arrays.asList(message.split(" "));
    }

    public static String getMentionId(String arg) {
        return arg.replace("@", "").replace("<", "").replace(">", "");
    }
}
