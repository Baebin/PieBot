package com.piebin.piebot.model.entity;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.emoji.Emoji;

@AllArgsConstructor
public enum UniEmoji {
    CHECK("✅"),

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
}
