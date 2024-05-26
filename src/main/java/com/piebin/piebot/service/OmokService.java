package com.piebin.piebot.service;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.OmokRoom;
import com.piebin.piebot.model.entity.OmokState;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface OmokService {
    EmbedBuilder getProfile(Account account);
    String createBoard(OmokRoom omokRoom);

    void addWin(Account account);
    void addTie(Account account);
    void addLose(Account account);

    boolean isWin(OmokRoom omokRoom, OmokState state, char x, int y);
    void selectPosition(MessageReceivedEvent event, Account account, char x, int y);
}
