package com.piebin.piebot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandErrorSentence {
    DICE_ARG1("주사위 굴리기", "주사위 범위가 올바르지 않습니다.", "[2~6] 사이의 수를 입력해주세요.");

    private String title;
    private String message;
    private String description;
}
