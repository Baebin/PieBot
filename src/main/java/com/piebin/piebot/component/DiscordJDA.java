package com.piebin.piebot.component;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    public Optional<Message> getMessageByID(String channelId, String messageId) {
        try {
            Optional<TextChannel> optional = getTextChannelByID(channelId);
            if (optional.isPresent()) {
                TextChannel channel = optional.get();
                return Optional.of(channel.retrieveMessageById(messageId).complete());
            }
        } catch (Exception e) {}
        return Optional.empty();
    }

    private Optional<Message> getMessageByID(TextChannel channel, String id) {
        try {
            return Optional.of(channel.retrieveMessageById(id).complete());
        } catch (Exception e) {}
        return Optional.empty();
    }
}
