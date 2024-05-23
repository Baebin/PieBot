package com.piebin.piebot.service.impl;

import com.piebin.piebot.model.entity.CommandMode;
import com.piebin.piebot.model.entity.CommandParameter;
import com.piebin.piebot.service.CommandService;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.service.impl.commands.EmbedPrintCommand;
import com.piebin.piebot.utility.CommandHelper;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommandServiceImpl implements CommandService {
    public static final String PREFIX = "ㅋ";

    private boolean checkArg(String arg, CommandParameter commandParameter) {
        for (String data : commandParameter.getData()) {
            if (
                    (commandParameter.getMode() == CommandMode.EQUAL && arg.equalsIgnoreCase(data))
                    || (commandParameter.getMode() == CommandMode.CONTAIN && arg.toLowerCase().contains(data))
            ) return true;
        }
        return false;
    }

    @Override
    public void runCommand(MessageReceivedEvent event) {
        User user = event.getAuthor();
        if (user.isBot())
            return;
        List<String> args = CommandHelper.getArgs(event);
        log.info("user: {}, args: {}", user, args);

        if (args.get(0).equals(PREFIX)) {
            if (args.size() == 1)
                return;
            for (CommandParameter parameter : CommandParameter.values()) {
                if (!checkArg(args.get(1), parameter))
                    continue;
                PieCommand command = parameter.getCommand();
                command.execute(event);
                break;
            }
        }
    }
}
