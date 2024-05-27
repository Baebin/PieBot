package com.piebin.piebot.model.entity;

import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.service.impl.commands.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandParameter {
    PROFILE(null, new String[] { "profile", "프로필" }, null, "프로필을 보여줍니다.", CommandMode.EQUAL),
    PATCH_NOTE(null, new String[] { "patch", "패치" }, "[페이지]", "패치 노트를 보여줍니다.", CommandMode.CONTAIN),
    HELP(new HelpCommand(), new String[] { "help", "도움말", "명령어" }, "[페이지]", "명령어 목록을 보여줍니다.", CommandMode.EQUAL),

    PAY(null, new String[] { "pay", "송금" }, "[유저] [금액]", "상대방에게 돈을 송금합니다.", CommandMode.EQUAL),
    MONEY_RANK(new MoneyRankCommand(), new String[] { "money", "돈", "자산" }, "[페이지]", "유저들의 보유 자산 순위를 보여줍니다.", CommandMode.EQUAL),

    BABO(new BaboCommand(), new String[] { "babo", "바보" }, null, "바보를 출력합니다.", CommandMode.EQUAL),
    DICE(new DiceCommand(), new String[] { "dice", "주사위" }, "[2~100]", "주사위를 굴립니다.", CommandMode.EQUAL),

    OMOK_PVP(null, new String[] { "omok", "오목" }, "[pvp/대전] [유저]", "오목 대전을 신청합니다.", CommandMode.EQUAL),
    OMOK_QUIT(null, new String[] { "omok", "오목" }, "[quit/퇴장]", "오목 게임을 종료합니다.", CommandMode.EQUAL),
    OMOK_RANK(null, new String[] { "omok", "오목" }, "[rank/순위] [페이지]", "유저들의 오목 순위를 보여줍니다.", CommandMode.EQUAL),
    OMOK_PROFILE(null, new String[] { "omok", "오목" }, "[profile/프로필]", "오목 게임 프로필을 보여줍니다.", CommandMode.EQUAL),
    OMOK_CONTINUE(null, new String[] { "omok", "오목" }, "[continue/이어하기]", "오목 게임을 재개합니다.", CommandMode.EQUAL),

    GAMBLING_MUKCHIBA(null, new String[] { "gambling", "도박" }, "[묵찌빠] [금액] [묵/찌/빠]", "묵찌바 게임을 시작합니다.", CommandMode.EQUAL),

    SECRET_EASTEREGG(null, new String[] { "이스터에그", "에그머니나" }, null, "잘 찾아보라구 ~", CommandMode.CONTAIN),
    SECRET_EASTEREGG_LIST(null, new String[] { "전당", "명예전당", "명예의전당" }, null, "이 기록을 빙구에게 바칩니다.", CommandMode.CONTAIN),

    SECRET_FOOD(new SecretFoodCommand(), new String[] { "answer", "정답" }, "Secret", "시크릿 명령어입니다.", CommandMode.CONTAIN);

    private final PieCommand command;

    private final String[] data;
    private final String args;
    private final String description;

    private final CommandMode mode;
}
