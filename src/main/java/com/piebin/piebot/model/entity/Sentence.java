package com.piebin.piebot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Sentence {
    COMMAND_DICE_RESULT("주사위 결과"),
    SECRET_FOOD("음식 맞추기"),

    EASTER_EGG_LIST("최초 발견자");

    String message;
}
