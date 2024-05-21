package com.piebin.piebot.service.impl;

import com.piebin.piebot.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommandServiceImpl implements CommandService {
    @Override
    public void sendBabo(MessageReceivedEvent event) {
        User user = event.getAuthor();
        TextChannel channel = event.getChannel().asTextChannel();
        channel.sendMessage(user.getEffectiveName() + " 바보.").queue();
    }
}
