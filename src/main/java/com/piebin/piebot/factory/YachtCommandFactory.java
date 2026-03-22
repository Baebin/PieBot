package com.piebin.piebot.factory;

import java.util.List;

public interface YachtCommandFactory {
    List<String> getChoiceCommands();
    List<String> getFourOfAKindCommands();
    List<String> getFullHouseCommands();
    List<String> getSmallStraightCommands();
    List<String> getLargeStraightCommands();
    List<String> getYachtCommands();
}
