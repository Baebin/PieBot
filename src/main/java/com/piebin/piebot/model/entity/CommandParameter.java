package com.piebin.piebot.model.entity;

import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.service.impl.commands.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

@Getter
@AllArgsConstructor
public enum CommandParameter {
    TEST(new BaboCommand(), new String[] { "babo", "바보" }, null, "바보를 출력합니다.", CommandMode.EQUAL),
    DICE(new DiceCommand(), new String[] { "dice", "주사위" }, "[2~6]", "주사위를 굴립니다.", CommandMode.EQUAL),
    HELP(new HelpCommand(), new String[] { "help", "명령어" }, null, "명령어 목록을 보여줍니다.", CommandMode.EQUAL),

    SECRET_EASTEREGG(new EmbedPrintCommand(CommandSentence.SECRET_EASTEREGG, Color.GREEN), new String[] { "이스터에그", "에그머니나" }, null, "잘 찾아보라구 ~", CommandMode.CONTAIN),

    SECRET_FOOD(new SecretFoodCommand(), new String[] { "answer", "정답" }, "Secret", "시크릿 명령어입니다.", CommandMode.CONTAIN);

    private final PieCommand command;

    private final String[] data;
    private final String args;
    private final String description;

    private CommandMode mode;
}
