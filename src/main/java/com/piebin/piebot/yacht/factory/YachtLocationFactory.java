package com.piebin.piebot.yacht.factory;

import kotlin.Pair;

import java.util.List;

public interface YachtLocationFactory {
    int getSectionX();
    int getSectionXWeight();
    List<Pair<Integer, Integer>> getSelectedDiceLocations();
    Pair<Integer, Integer> getSelectedDiceLocation(int number);
    List<Pair<Integer, Integer>> getNonSelectedDiceLocations();
    List<Pair<Integer, Integer>> getRandomDiceLocations();
    Pair<Integer, Integer> getRandomDiceLocation(int number);
    List<Pair<Integer, Integer>> getNumberLocations();
    Pair<Integer, Integer> getNumberLocation(int number);
    Pair<Integer, Integer> getBonusLocation();
    Pair<Integer, Integer> getChoiceLocation();
    Pair<Integer, Integer> getFourOfAKindLocation();
    Pair<Integer, Integer> getFullHouseLocation();
    Pair<Integer, Integer> getSmallStraightLocation();
    Pair<Integer, Integer> getLargeStraightHouseLocation();
    Pair<Integer, Integer> getYachtLocation();
    Pair<Integer, Integer> getTotalPointsLocation();
}
