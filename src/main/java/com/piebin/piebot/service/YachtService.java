package com.piebin.piebot.service;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.Yacht;
import com.piebin.piebot.model.domain.YachtRoom;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.io.File;

public interface YachtService {
    File getBoard(YachtRoom yachtRoom);
    String getBoardString(YachtRoom yachtRoom);

    void invitePVP(MessageReceivedEvent event);
    void createYachtRoom(MessageReactionAddEvent event);
    void quitYachtRoom(MessageReceivedEvent event);

    Yacht getOrCreateYacht(Account account);
    void addWin(Account account);
    void addTie(Account account);
    void addLose(Account account);
}
