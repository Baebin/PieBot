package com.piebin.piebot.model.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DiscordEmoji {
    FRUIT_APPLE("fruit_apple", "1244952446275555390", false),
    FRUIT_MANGO("fruit_mango", "1244952480522043403", false),
    FRUIT_PLUM("fruit_plum", "1244952507734560808", false),
    FRUIT_PITCH("fruit_pitch", "1244952787268141158", false),
    FRUIT_COCONUT("fruit_coconut", "1244952815844065311", false),
    FRUIT_RANDOM("fruit_random", "1244955151836516423", true);

    private final String emoji;
    private final String id;

    private final boolean isGif;

    public String getEmoji() {
        if (isGif)
            return "<a:" + emoji + ":" + id + ">";
        return "<:" + emoji + ":" + id + ">";
    }
}
