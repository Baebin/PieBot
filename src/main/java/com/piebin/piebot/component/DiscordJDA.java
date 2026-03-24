package com.piebin.piebot.component;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Consumer;

@Getter
@Setter
@Component
public class DiscordJDA {
    private JDA jda;

    public Optional<TextChannel> getTextChannelByID(String id) {
        try {
            return Optional.of(jda.getTextChannelById(id));
        } catch (Exception e) {}
        return Optional.empty();
    }

    public void retrieveMessageByID(String channelId, String messageId, Consumer<Message> consumer) {
        try {
            Optional<TextChannel> optional = getTextChannelByID(channelId);
            if (optional.isPresent()) {
                TextChannel channel = optional.get();
                channel.retrieveMessageById(messageId).queue((message) -> {
                    if (message == null)
                        return;
                    consumer.accept(message);
                });
            }
        } catch (Exception e) {}
    }
}
