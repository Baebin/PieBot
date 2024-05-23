package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.service.PieCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BaboCommand implements PieCommand {
    @Override
    public void execute(MessageReceivedEvent event) {
        User user = event.getAuthor();
        TextChannel channel = event.getChannel().asTextChannel();
        Member member = event.getGuild().retrieveMemberById(user.getId()).complete();
        channel.sendMessage(member.getNickname() + " 바보.").queue();
    }
}
