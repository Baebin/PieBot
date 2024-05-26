package com.piebin.piebot.utility;

import com.piebin.piebot.model.entity.UniEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;

public class EmojiManager {
    public static int getPageCount(Emoji emoji) {
        if (emoji.equals(UniEmoji.ARROW_LEFT_DOUBLE.getEmoji()))
            return -10;
        if (emoji.equals(UniEmoji.ARROW_LEFT.getEmoji()))
            return -1;
        if (emoji.equals(UniEmoji.ARROW_REFRESH.getEmoji()))
            return 0;
        if (emoji.equals(UniEmoji.ARROW_RIGHT.getEmoji()))
            return 1;
        if (emoji.equals(UniEmoji.ARROW_RIGHT_DOUBLE.getEmoji()))
            return 10;
        return 0;
    }

    public static int getNumber(Emoji emoji) {
        if (emoji.equals(UniEmoji.NUM_0.getEmoji()))
            return 0;
        if (emoji.equals(UniEmoji.NUM_1.getEmoji()))
            return 1;
        if (emoji.equals(UniEmoji.NUM_2.getEmoji()))
            return 2;
        if (emoji.equals(UniEmoji.NUM_3.getEmoji()))
            return 3;
        if (emoji.equals(UniEmoji.NUM_4.getEmoji()))
            return 4;
        if (emoji.equals(UniEmoji.NUM_5.getEmoji()))
            return 5;
        if (emoji.equals(UniEmoji.NUM_6.getEmoji()))
            return 6;
        if (emoji.equals(UniEmoji.NUM_7.getEmoji()))
            return 7;
        if (emoji.equals(UniEmoji.NUM_8.getEmoji()))
            return 8;
        if (emoji.equals(UniEmoji.NUM_9.getEmoji()))
            return 9;
        return 0;
    }

    public static Emoji getEmoji(int num) {
        if (num == 0)
            return UniEmoji.NUM_0.getEmoji();
        if (num == 1)
            return UniEmoji.NUM_1.getEmoji();
        if (num == 2)
            return UniEmoji.NUM_2.getEmoji();
        if (num == 3)
            return UniEmoji.NUM_3.getEmoji();
        if (num == 4)
            return UniEmoji.NUM_4.getEmoji();
        if (num == 5)
            return UniEmoji.NUM_5.getEmoji();
        if (num == 6)
            return UniEmoji.NUM_6.getEmoji();
        if (num == 7)
            return UniEmoji.NUM_7.getEmoji();
        if (num == 8)
            return UniEmoji.NUM_8.getEmoji();
        if (num == 9)
            return UniEmoji.NUM_9.getEmoji();
        return null;
    }
}
