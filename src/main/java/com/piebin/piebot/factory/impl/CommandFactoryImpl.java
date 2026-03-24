package com.piebin.piebot.factory.impl;

import com.piebin.piebot.factory.CommandFactory;
import com.piebin.piebot.model.entity.CommandParameter;
import com.piebin.piebot.service.PieCommand;
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
