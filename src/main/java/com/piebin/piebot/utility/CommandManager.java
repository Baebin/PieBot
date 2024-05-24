package com.piebin.piebot.utility;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class CommandManager {
    public static List<String> getArgs(MessageReceivedEvent event) {
        return Arrays.asList(event.getMessage().getContentRaw().split(" "));
    }

    public static String getMentionId(String arg) {
        return arg.replace("@", "").replace("<", "").replace(">", "");
    }
}
