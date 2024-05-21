package com.piebin.piebot;

import com.piebin.piebot.entity.DiscordBotToken;
import com.piebin.piebot.listener.CommandListener;
import com.piebin.piebot.service.impl.CommandServiceImpl;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class PieBotApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(PieBotApplication.class, args);
        DiscordBotToken token = context.getBean(DiscordBotToken.class);

        JDABuilder.createDefault(token.getToken())
                .setActivity(Activity.playing("나는 바보"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new CommandListener(new CommandServiceImpl()))
                .build();
    }
}
