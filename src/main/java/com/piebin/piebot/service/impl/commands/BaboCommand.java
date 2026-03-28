package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.service.PieCommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class BaboCommand implements PieCommand {
    @Override
    public void execute(MessageReceivedEvent event) {
        User user = event.getAuthor();
        event.getGuild().retrieveMemberById(user.getId()).queue((member) -> {
            String name = member.getNickname();
            if (ObjectUtils.isEmpty(name))
                name = member.getEffectiveName();
            event.getMessage().reply(name + " 바보.").queue();
        });
    }
}
