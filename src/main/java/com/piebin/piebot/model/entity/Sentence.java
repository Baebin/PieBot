package com.piebin.piebot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Sentence {
    COMMAND_DICE_RESULT("주사위 결과"),
    SECRET_FOOD("음식 맞추기"),

    EASTER_EGG_LIST("최초 발견자"),
    IS_EASTER_EGG("번째 이스터에그야."),

    PROFILE("프로필"),
    
    HELP("명령어 목록"),
    HELP_1("명령어 목록 - 기본"),
    HELP_2("명령어 목록 - 오락"),
    HELP_3("명령어 목록 - 화폐"),
    HELP_4("명령어 목록 - 기타"),

    OMOK("오목"),
    OMOK_RANK("오목 순위"),

    MONEY_RANK("자산 순위"),
    RANK_REFRESH("마지막 갱신 시간"),

    REGISTER("회원가입");

    private final String message;
}
