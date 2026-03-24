package com.piebin.piebot.factory;

import com.piebin.piebot.model.entity.CommandParameter;
import com.piebin.piebot.service.PieCommand;

public interface CommandFactory {
    PieCommand getCommand(CommandParameter parameter);
}
