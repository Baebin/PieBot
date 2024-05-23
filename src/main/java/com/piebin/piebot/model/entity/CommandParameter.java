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

    // Easter Egg
    SECRET_TAZO(new EmbedPrintCommand(CommandSentence.SECRET_TAZO, Color.GREEN), new String[] { "xkwh", "타조" }, "Secret", "Easter Egg 1", CommandMode.CONTAIN),
    SECRET_KOOKOO(new EmbedPrintCommand(CommandSentence.SECRET_KOOKOO, Color.GREEN), new String[] { "삥뽕", "쿠쿠루" }, "Secret", "Easter Egg 2", CommandMode.CONTAIN),
    SECRET_KOOKOO2(new EmbedPrintCommand(CommandSentence.SECRET_KOOKOO2, Color.GREEN), new String[] { "빼옹" }, "Secret", "Easter Egg 3", CommandMode.CONTAIN),
    SECRET_YABBEE(new EmbedPrintCommand(CommandSentence.SECRET_YABBEE, Color.GREEN), new String[] { "얍", "삐" }, "Secret", "Easter Egg 4", CommandMode.CONTAIN),
    SECRET_DANMOOJI(new EmbedPrintCommand(CommandSentence.SECRET_DANMOOJI, Color.GREEN), new String[] { "무지" }, "Secret", "Easter Egg 5", CommandMode.CONTAIN),
    SECRET_BINGSIN(new EmbedPrintCommand(CommandSentence.SECRET_BINGSIN, Color.GREEN), new String[] { "빙신" }, "Secret", "Easter Egg 6", CommandMode.CONTAIN),
    SECRET_PAENGSIN(new EmbedPrintCommand(CommandSentence.SECRET_PAENGSIN, Color.GREEN), new String[] { "핑신" }, "Secret", "Easter Egg 7", CommandMode.CONTAIN),
    SECRET_ULEOG(new EmbedPrintCommand(CommandSentence.SECRET_ULEOG, Color.GREEN), new String[] { "우럭탕", "매운탕", "우럭매운탕", "매운우럭탕" }, "Secret", "Easter Egg 8", CommandMode.CONTAIN),
    SECRET_HAWAWA(new EmbedPrintCommand(CommandSentence.SECRET_HAWAWA, Color.GREEN), new String[] { "하와와" }, "Secret", "Easter Egg 9", CommandMode.CONTAIN),
    SECRET_BUIKK(new EmbedPrintCommand(CommandSentence.SECRET_BUIKK, Color.GREEN), new String[] { "븪" }, "Secret", "Easter Egg 10", CommandMode.CONTAIN),
    SECRET_COOKIE(new EmbedPrintCommand(CommandSentence.SECRET_BUIKK, Color.GREEN), new String[] { "머랭", "쿠키" }, "Secret", "Easter Egg 11", CommandMode.CONTAIN),
    SECRET_RESET(new EmbedPrintCommand(CommandSentence.SECRET_BUIKK, Color.GREEN), new String[] { "reset", "리셋" }, "Secret", "Easter Egg 12", CommandMode.CONTAIN),
    SECRET_POTATO(new EmbedPrintCommand(CommandSentence.SECRET_POTATO, Color.GREEN), new String[] { "Potato", "감자", "감쟈", "걈자", "걈쟈" }, "Secret", "Easter Egg 13", CommandMode.CONTAIN),
    SECRET_KIMCHICHESSEUDONG(new EmbedPrintCommand(CommandSentence.SECRET_KIMCHICHESSEUDONG, Color.GREEN), new String[] { "김치우동", "치즈우동", "김치치즈우동", "치즈김치우동" }, "Secret", "Easter Egg 14", CommandMode.CONTAIN),
    SECRET_BINGDAL(new EmbedPrintCommand(CommandSentence.SECRET_BINGDAL, Color.GREEN), new String[] { "빙달어" }, "Secret", "Easter Egg 15", CommandMode.CONTAIN),
    SECRET_JACKYGYU(new EmbedPrintCommand(CommandSentence.SECRET_JACKYGYU, Color.GREEN), new String[] { "제키교" }, "Secret", "Easter Egg 16", CommandMode.CONTAIN),
    SECRET_TARO(new EmbedPrintCommand(CommandSentence.SECRET_TARO, Color.GREEN), new String[] { "타로" }, "Secret", "Easter Egg 17", CommandMode.CONTAIN),
    SECRET_CHOPIN(new EmbedPrintCommand(CommandSentence.SECRET_CHOPIN, Color.GREEN), new String[] { "쇼팽" }, "Secret", "Easter Egg 18", CommandMode.CONTAIN),
    SECRET_YAJEONSAB(new EmbedPrintCommand(CommandSentence.SECRET_YAJEONSAB, Color.GREEN), new String[] { "야전삽" }, "Secret", "Easter Egg 19", CommandMode.CONTAIN),

    SECRET_EASTEREGG(new EmbedPrintCommand(CommandSentence.SECRET_EASTEREGG, Color.GREEN), new String[] { "이스터에그", "에그머니나" }, null, "잘 찾아보라구 ~", CommandMode.CONTAIN),

    SECRET_FOOD(new SecretFoodCommand(), new String[] { "answer", "정답" }, "Secret", "시크릿 명령어입니다.", CommandMode.CONTAIN);

    private final PieCommand command;

    private final String[] data;
    private final String args;
    private final String description;

    private CommandMode mode;
}
