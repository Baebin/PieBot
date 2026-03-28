package com.piebin.piebot.yacht.factory.impl;

import com.piebin.piebot.yacht.factory.YachtCommandFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class YachtCommandFactoryImpl implements YachtCommandFactory {
    @Bean
    @Override
    public List<String> getChoiceCommands() {
        return Arrays.asList(
                "c",
                "ch",
                "cho",
                "choice",
                "초",
                "초이",
                "초이스"
        );
    }

    @Bean
    @Override
    public List<String> getFourOfAKindCommands() {
        return Arrays.asList(
                "four",
                "fourOfAKind",
                "포카"
        );
    }

    @Bean
    @Override
    public List<String> getFullHouseCommands() {
        return Arrays.asList(
                "FH",
                "FullHouse",
                "풀",
                "풀하",
                "풀하우스"
        );
    }

    @Bean
    @Override
    public List<String> getSmallStraightCommands() {
        return Arrays.asList(
                "s",
                "ss",
                "small",
                "SmallStraight",
                "스스",
                "스몰",
                "스몰스트레이트"
        );
    }

    @Bean
    @Override
    public List<String> getLargeStraightCommands() {
        return Arrays.asList(
                "l",
                "lar",
                "large",
                "LargeStraight",
                "라스",
                "라지",
                "라지스트레이트"
        );
    }

    @Bean
    @Override
    public List<String> getYachtCommands() {
        return Arrays.asList(
                "y",
                "ya",
                "yacht",
                "야추",
                "요트"
        );
    }
}
