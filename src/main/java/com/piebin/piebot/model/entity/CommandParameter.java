package com.piebin.piebot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandParameter {
    TEST(new String[] {
            "test", "테스트"
    });

    private final String[] data;
}
