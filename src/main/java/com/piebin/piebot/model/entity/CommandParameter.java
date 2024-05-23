package com.piebin.piebot.model.entity;

import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.service.impl.commands.BaboCommand;
import com.piebin.piebot.service.impl.commands.DiceCommand;
import com.piebin.piebot.service.impl.commands.HelpCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandParameter {
    TEST(new BaboCommand(), new String[] { "babo", "바보" }, null, "바보를 출력합니다."),
    DICE(new DiceCommand(), new String[] { "dice", "주사위" }, "[2~6]", "주사위를 굴립니다."),
    HELP(new HelpCommand(), new String[] { "help", "명령어" }, null, "명령어 목록을 보여줍니다.");

    private final PieCommand command;

    private final String[] data;
    private final String args;
    private final String description;
}
