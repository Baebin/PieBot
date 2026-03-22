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
    boolean selectNumberScore(YachtRoom yachtRoom, int number);
    boolean selectBonusScore(YachtRoom yachtRoom);
    boolean selectChoiceScore(YachtRoom yachtRoom);
    boolean selectFourOfAKindScore(YachtRoom yachtRoom);
    boolean selectFullHouseScore(YachtRoom yachtRoom);
    boolean selectSmallStraightScore(YachtRoom yachtRoom);
    boolean selectLargeStraightScore(YachtRoom yachtRoom);
    boolean selectYachtScore(YachtRoom yachtRoom);

    boolean selectDice(YachtRoom yachtRoom, int number);
    boolean deselectDice(YachtRoom yachtRoom, int number);
    boolean rollDices(YachtRoom yachtRoom);

    void invitePVP(MessageReceivedEvent event);
    void createYachtRoom(MessageReactionAddEvent event);
    void quitYachtRoom(MessageReceivedEvent event);
    void continueYachtRoom(MessageReceivedEvent event);

    Yacht getOrCreateYacht(Account account);
    void addWin(Account account);
    void addTie(Account account);
    void addLose(Account account);
}
