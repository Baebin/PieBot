package com.piebin.piebot.global.entity;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.emoji.Emoji;

@AllArgsConstructor
public enum UniEmoji {
    CHECK("✅"),

    ARROW_LEFT("⬅️"),
    ARROW_RIGHT("➡️"),
    ARROW_LEFT_DOUBLE("⏪"),
    ARROW_RIGHT_DOUBLE("⏩"),
    ARROW_REFRESH("\uD83D\uDD04"),

    ARROW_HEADING_UP("⤴\uFE0F"),
    ARROW_HEADING_DOWN("⤵\uFE0F"),

    RECYCLE("♻\uFE0F"),

    SMALL_RED_TRIANGLE("\uD83D\uDD3A"),
    SMALL_RED_TRIANGLE_DOWN("\uD83D\uDD3B"),

    NUM_0("0️⃣"),
    NUM_1("1️⃣"),
    NUM_2("2️⃣"),
    NUM_3("3️⃣"),
    NUM_4("4️⃣"),
    NUM_5("5️⃣"),
    NUM_6("6️⃣"),
    NUM_7("7️⃣"),
    NUM_8("8️⃣"),
    NUM_9("9️⃣");

    private final String emoji;

    public Emoji getEmoji() {
        return Emoji.fromUnicode(this.emoji);
    }

    @Override
    public String toString() {
        return emoji;
    }
}
