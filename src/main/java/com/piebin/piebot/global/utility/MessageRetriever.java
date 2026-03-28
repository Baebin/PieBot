package com.piebin.piebot.global.utility;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.util.Optional;
import java.util.function.Consumer;

public interface MessageRetriever {
    Optional<MessageEmbed> getEmbed(Message message);
    void retrieveMessage(MessageReactionAddEvent event, Consumer<Message> consumer) ;
    void retrieveMessageEmbed(MessageReactionAddEvent event, Consumer<MessageEmbed> consumer) ;
}
