package com.piebin.piebot.global.service;

import com.piebin.piebot.global.domain.Account;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public interface AccountService {
    void register(TextChannel channel, Member member);
    boolean existsUser(String id);
    Account findUser(String id);
}
