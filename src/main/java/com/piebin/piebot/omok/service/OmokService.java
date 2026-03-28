package com.piebin.piebot.omok.service;

import com.piebin.piebot.global.domain.Account;
import com.piebin.piebot.omok.domain.OmokRoom;
import com.piebin.piebot.omok.entity.OmokState;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public interface OmokService {
    EmbedBuilder getProfile(Account account);
    String createBoard(OmokRoom omokRoom);

    void createOmokRoom(MessageReactionAddEvent event, Message message);

    void addWin(Account account);
    void addTie(Account account);
    void addLose(Account account);

    boolean isWin(OmokRoom omokRoom, OmokState state, char x, int y);
    void selectPosition(MessageReceivedEvent event, Account account, char x, int y);
}
