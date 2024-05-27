package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.service.PieCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class BaboCommand implements PieCommand {
    @Override
    public void execute(MessageReceivedEvent event) {
        User user = event.getAuthor();
        Member member = event.getGuild().retrieveMemberById(user.getId()).complete();
        event.getMessage().reply(member.getNickname() + " 바보.").queue();
    }
}
