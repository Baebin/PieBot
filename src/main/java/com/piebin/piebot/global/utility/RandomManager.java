package com.piebin.piebot.global.utility;

import lombok.experimental.UtilityClass;
import org.springframework.context.annotation.Bean;
import java.util.Random;

@UtilityClass
public class RandomManager {
    @Bean
    // Weight [0, 100]
    public static boolean isPass(int weight) {
        weight = Math.max(0, weight);
        weight = Math.min(weight, 100);

        // w [0, 99]
        int w = new Random().nextInt(100);
        return (weight < w);
    }

    // Sum of Weights : 100
    public static int get(int ... weights) {
        // w [0, 99]
        int w = new Random().nextInt(100);
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] == 0)
                continue;
            sum += weights[i];
            if (w < sum)
                return i;
        }
        return -1;
    }

    public static int nextInt(int i) {
        return new Random().nextInt(i);
    }

    // [i, i + count]
    public static int nextInt(int i, int count) {
        return new Random().nextInt(count) + i;
    }
}
