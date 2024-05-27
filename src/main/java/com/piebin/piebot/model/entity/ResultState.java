package com.piebin.piebot.model.entity;

public enum ResultState {
    WIN, TIE, LOSE;

    public String getResult() {
        if (this == ResultState.WIN)
            return "승";
        if (this == ResultState.TIE)
            return "무";
        return "패";
    }

    public int getWeight() {
        if (this == ResultState.WIN)
            return 1;
        if (this == ResultState.TIE)
            return 0;
        return -1;
    }
}
