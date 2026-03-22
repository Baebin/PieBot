package com.piebin.piebot.service;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.Yacht;
import com.piebin.piebot.model.domain.YachtRoom;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public interface YachtService {
    String getBoardString(YachtRoom yachtRoom);

    Message sendYachtRoomMessage(MessageChannel channel, YachtRoom yachtRoom);
    void editYachtRoomMessage(YachtRoom yachtRoom);

    void selectEmoji(MessageReactionAddEvent event);

    void select(MessageReceivedEvent event, String type);
    void selectNumberScore(YachtRoom yachtRoom, int number);
    void selectBonusScore(YachtRoom yachtRoom);
    void selectChoiceScore(YachtRoom yachtRoom);
    void selectFourOfAKindScore(YachtRoom yachtRoom);
    void selectFullHouseScore(YachtRoom yachtRoom);
    void selectSmallStraightScore(YachtRoom yachtRoom);
    void selectLargeStraightScore(YachtRoom yachtRoom);
    void selectYachtScore(YachtRoom yachtRoom);

    void selectDice(YachtRoom yachtRoom, int number);
    void deselectDice(YachtRoom yachtRoom, int number);
    void rollDices(YachtRoom yachtRoom);

    void invitePVP(MessageReceivedEvent event);
    void createYachtRoom(MessageReactionAddEvent event);
    void quitYachtRoom(MessageReceivedEvent event);
    void continueYachtRoom(MessageReceivedEvent event);

    Yacht getOrCreateYacht(Account account);
    void addWin(Account account);
    void addTie(Account account);
    void addLose(Account account);
}
