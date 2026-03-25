package com.piebin.piebot.utility;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.entity.Sentence;
import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

@UtilityClass
public class ProfileAnalyzer {
    public static EmbedBuilder getProfile(Account account, long win, long tie, long lose) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Sentence.PROFILE.getMessage());
        embedBuilder.setColor(Color.GREEN);

        long total = (win + tie + lose);
        double odds = 0.0;
        if (total != 0)
            odds = (100 * win / total);
        embedBuilder.addField("이름", account.getName(), false);
        embedBuilder.addField("승률", String.format("%.2f", odds) + "%", false);
        String value = NumberManager.getNumber(win) + "승 "
                + NumberManager.getNumber(tie) + "무 "
                + NumberManager.getNumber(lose) + "패";
        embedBuilder.addField("전적", value, false);

        return embedBuilder;
    }
}
