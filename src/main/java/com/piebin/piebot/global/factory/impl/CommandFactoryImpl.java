package com.piebin.piebot.global.factory.impl;

import com.piebin.piebot.global.factory.CommandFactory;
import com.piebin.piebot.global.entity.CommandParameter;
import com.piebin.piebot.global.service.PieCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandFactoryImpl implements CommandFactory {
    private final ApplicationContext applicationContext;

    @Override
    public PieCommand getCommand(CommandParameter parameter) {
        if (parameter.getCommand() == null) return null;
        return applicationContext.getBean(parameter.getCommand());
    }
}
