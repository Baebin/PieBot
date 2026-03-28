package com.piebin.piebot.global.factory;

import com.piebin.piebot.global.entity.CommandParameter;
import com.piebin.piebot.global.service.PieCommand;

public interface CommandFactory {
    PieCommand getCommand(CommandParameter parameter);
}
