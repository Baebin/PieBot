package com.piebin.piebot.model.entity;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;

@AllArgsConstructor
public enum DiscordEmoji {
    FRUIT_APPLE("fruit_apple", "1244952446275555390", false),
    FRUIT_MANGO("fruit_mango", "1244952480522043403", false),
    FRUIT_PLUM("fruit_plum", "1244952507734560808", false),
    FRUIT_PITCH("fruit_pitch", "1244952787268141158", false),
    FRUIT_COCONUT("fruit_coconut", "1244952815844065311", false),
    FRUIT_RANDOM("fruit_random", "1244955151836516423", true),

    MUKCHIBA_ROCK("mukchiba_rock", "1245734438445514803", false),
    MUKCHIBA_PAPER("mukchiba_paper", "1245734454090141726", false),
    MUKCHIBA_SCISSORS("mukchiba_scissors", "1245734467109126185", false),
    MUKCHIBA_RANDOM("mukchiba_random", "1245734488235966484", true);

    private final String name;
    private final String id;

    private final boolean isGif;

    public CustomEmoji getEmoji() {
        return Emoji.fromCustom(name, Long.parseLong(id), isGif);
    }

    @Override
    public String toString() {
        if (isGif)
            return "<a:" + name + ":" + id + ">";
        return "<:" + name + ":" + id + ">";
    }

    public boolean equals(Emoji emoji) {
        return getEmoji().getAsReactionCode().equals(emoji.getAsReactionCode());
    }
}
