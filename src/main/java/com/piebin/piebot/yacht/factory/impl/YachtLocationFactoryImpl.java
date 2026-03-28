package com.piebin.piebot.yacht.factory.impl;

import com.piebin.piebot.yacht.factory.YachtLocationFactory;
import kotlin.Pair;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Configuration
public class YachtLocationFactoryImpl implements YachtLocationFactory {
    private static int SECTION_X = 471;
    private static int SECTION_X_WEIGHT = 116;

    private static int NON_SELECTED_DICE_X_WEIGHT = 30;
    private static int NON_SELECTED_DICE_Y_WEIGHT = 15;

    @Override
    public int getSectionX() {
        return SECTION_X;
    }

    @Bean
    @Override
    public int getSectionXWeight() {
        return SECTION_X_WEIGHT;
    }

    @Bean
    @Override
    public List<Pair<Integer, Integer>> getSelectedDiceLocations() {
        return Arrays.asList(
                new Pair<>(849, 110), new Pair<>(952, 110),
                new Pair<>(1055, 110), new Pair<>(1157, 110), new Pair<>(1261, 110)
        );
    }

    @Override
    public Pair<Integer, Integer> getSelectedDiceLocation(int number) {
        return getSelectedDiceLocations().get(number - 1);
    }

    @Bean
    @Override
    public List<Pair<Integer, Integer>> getNonSelectedDiceLocations() {
        return Arrays.asList(
                new Pair<>(900, 310), new Pair<>(1060, 310),
                new Pair<>(900, 470), new Pair<>(1060, 470), new Pair<>(980, 630)
        );
    }

    @Override
    public List<Pair<Integer, Integer>> getRandomDiceLocations() {
        List<Pair<Integer, Integer>> locations = new ArrayList<>();
        for (int i = 1; i <= 5; i++)
            locations.add(getRandomDiceLocation(i));
        return locations;
    }

    @Override
    public Pair<Integer, Integer> getRandomDiceLocation(int number) {
        Pair<Integer, Integer> p = getNonSelectedDiceLocations().get(number - 1);
        Random random = new Random();
        return new Pair<>(
                p.getFirst() + random.nextInt(NON_SELECTED_DICE_X_WEIGHT) * (random.nextBoolean() ? 1 : -1),
                p.getSecond() + random.nextInt(NON_SELECTED_DICE_Y_WEIGHT) * (random.nextBoolean() ? 1 : -1)
        );
    }

    /*
    Player Board
    */
    @Bean
    @Override
    public List<Pair<Integer, Integer>> getNumberLocations() {
        return Arrays.asList(
                new Pair<>(SECTION_X, 288), new Pair<>(SECTION_X, 323),
                new Pair<>(SECTION_X, 359), new Pair<>(SECTION_X, 394),
                new Pair<>(SECTION_X, 430), new Pair<>(SECTION_X, 466)
        );
    }

    @Override
    public Pair<Integer, Integer> getNumberLocation(int number) {
        return getNumberLocations().get(number - 1);
    }

    @Bean
    @Override
    public Pair<Integer, Integer> getBonusLocation() {
        return new Pair<>(SECTION_X, 506);
    }

    @Bean
    @Override
    public Pair<Integer, Integer> getChoiceLocation() {
        return new Pair<>(SECTION_X, 618);
    }

    @Bean
    @Override
    public Pair<Integer, Integer> getFourOfAKindLocation() {
        return new Pair<>(SECTION_X, 651);
    }

    @Bean
    @Override
    public Pair<Integer, Integer> getFullHouseLocation() {
        return new Pair<>(SECTION_X, 685);

    }

    @Bean
    @Override
    public Pair<Integer, Integer> getSmallStraightLocation() {
        return new Pair<>(SECTION_X, 719);
    }

    @Bean
    @Override
    public Pair<Integer, Integer> getLargeStraightHouseLocation() {
        return new Pair<>(SECTION_X, 754);
    }

    @Bean
    @Override
    public Pair<Integer, Integer> getYachtLocation() {
        return new Pair<>(SECTION_X, 788);
    }

    @Bean
    @Override
    public Pair<Integer, Integer> getTotalPointsLocation() {
        return new Pair<>(SECTION_X, 828);
    }
}
