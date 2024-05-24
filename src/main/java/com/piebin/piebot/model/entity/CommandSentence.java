package com.piebin.piebot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandSentence {
    DICE_ARG1("주사위 굴리기", "주사위 범위가 올바르지 않습니다.", "[2~6] 사이의 수를 입력해주세요."),

    // Secret
    SECRET_EASTEREGG("Q. 에그를 찾아서", "모든 이스터에그를 찾으면 빈이가 밥을 사준다는 전설이 있다구", "이스터에그는 총 19개나 있단다?"),

    // Secret (Food)
    SECRET_FOOD_ARG1("음식 맞추기", "음식 이름을 입력해주세요.", ""),
    SECRET_FOOD_SUCCESS("음식 맞추기", "맞았습니다.", "우와.. 대박"),
    SECRET_FOOD_FAILED("음식 맞추기", "틀렸습니다.", "화이팅 :D"),
    SECRET_FOOD_HINT1("음식 맞추기", "힌트 1", "이름에 '치즈'가 들어갑니다."),
    SECRET_FOOD_HINT2("음식 맞추기", "힌트 2", "면류입니다."),
    SECRET_FOOD_HINT3("음식 맞추기", "힌트 3", "겠나요.. 어디서 욕심 많은 달팽이가 !"),
    SECRET_FOOD_CONTAIN1("음식 맞추기", "힌트", "완전 근접했는걸? 이 빙구는 감동했어."),
    SECRET_FOOD_CONTAIN2("음식 맞추기", "힌트", "...겠나요 팽달씨?"),
    SECRET_FOOD_CONTAIN3("음식 맞추기", "힌트", "정녕 저를 삶아먹으실 계획인가요");

    private String title;
    private String message;
    private String description;
}
