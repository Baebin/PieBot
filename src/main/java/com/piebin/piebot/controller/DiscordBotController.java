package com.piebin.piebot.controller;

import com.piebin.piebot.component.DiscordBotToken;
import com.piebin.piebot.listener.CommandListener;
import com.piebin.piebot.service.impl.CommandServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DiscordBotController {
    private static final String API = "/api/bot/";

    private final DiscordBotToken token;

    private static JDA jda;

    private static boolean status = false;

    @GetMapping(API + "run")
    public ResponseEntity<Boolean> run() {
        if (status)
            return ResponseEntity.ok(false);
        jda = JDABuilder.createDefault(token.getToken())
                .setActivity(Activity.playing("나는 바보"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new CommandListener(new CommandServiceImpl()))
                .build();
        status = true;
        log.info("Discord Bot Started");
        return ResponseEntity.ok(true);
    }

    @GetMapping(API + "stop")
    public ResponseEntity<Boolean> stop() {
        if (!status)
            return ResponseEntity.ok(false);
        jda.shutdown();
        status = false;
        log.info("Discord Bot Stopped");
        return ResponseEntity.ok(true);
    }
}
