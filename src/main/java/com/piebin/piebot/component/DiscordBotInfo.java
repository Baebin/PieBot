package com.piebin.piebot.component;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class DiscordBotInfo {
    @Value("${discord.bot.token}")
    private String token;
    @Value("${discord.bot.id}")
    private String botId;
}
