package com.piebin.piebot.utility.impl;

import com.piebin.piebot.utility.MessageRetriever;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Consumer;

@Component
public class MessageRetrieverImpl implements MessageRetriever {
    @Override
    public Optional<MessageEmbed> getEmbed(Message message) {
        try {
            return Optional.of(message.getEmbeds().get(0));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void retrieveMessage(MessageReactionAddEvent event, Consumer<Message> consumer) {
        event.retrieveMessage().queue((message) -> {
            if (message == null)
                return;
            consumer.accept(message);
        });
    }

    @Override
    public void retrieveMessageEmbed(MessageReactionAddEvent event, Consumer<MessageEmbed> consumer) {
        retrieveMessage(event, (message) -> {
            Optional<MessageEmbed> optional = getEmbed(message);
            if (optional.isEmpty())
                return;
            consumer.accept(optional.get());
        });
    }
}
