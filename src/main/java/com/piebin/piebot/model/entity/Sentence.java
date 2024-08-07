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
    HELP_4("명령어 목록 - 아이템"),
    HELP_5("명령어 목록 - 기타"),

    OMOK("오목"),
    OMOK_RANK("오목 순위"),

    GAMBLING_MUKCHIBA("묵찌빠 게임"),
    GAMBLING_SLOTMACHINE("슬롯머신 게임"),

    INVENTORY("인벤토리"),

    SHOP("아이템 상점"),
    SHOP_DEFAULT("아이템 상점 - 일반"),
    SHOP_GAME("아이템 상점 - 게임"),
    SHOP_ETC("아이템 상점 - 기타"),

    MONEY_RANK("자산 순위"),
    ATTENDANCE_RANK("출석부 순위"),
    PATCH_NOTE("패치 노트"),
    CONTRIBUTOR("기여자"),

    RANK_REFRESH("마지막 갱신 시간"),

    REGISTER("회원가입");

    private final String message;
}
