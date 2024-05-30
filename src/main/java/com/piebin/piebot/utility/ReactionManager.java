package com.piebin.piebot.utility;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class ReactionManager {
    static public Message getMessage(MessageReactionAddEvent event) {
        try {
            return event.retrieveMessage().complete();
        } catch (Exception e) {
            return null;
        }
    }

    static public MessageEmbed getEmbed(Message message) {
        try {
            return message.getEmbeds().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    static public MessageEmbed getEmbed(MessageReactionAddEvent event) {
        try {
            Message message = event.retrieveMessage().complete();
            return message.getEmbeds().get(0);
        } catch (Exception e) {
            return null;
        }
    }
}
