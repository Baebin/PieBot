package com.piebin.piebot.controller;

import com.piebin.piebot.component.DiscordBotInfo;
import com.piebin.piebot.listener.CommandListener;
import com.piebin.piebot.listener.ReactionListener;
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

    private static JDA jda;

    private static boolean status = false;

    private final DiscordBotInfo botInfo;

    private final CommandListener commandListener;
    private final ReactionListener reactionListener;

    @GetMapping(API + "run")
    public ResponseEntity<Boolean> run() {
        if (status)
            return ResponseEntity.ok(false);
        jda = JDABuilder.createDefault(botInfo.getToken())
                .setActivity(Activity.playing("ㅋ help"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(
                        commandListener,
                        reactionListener
                )
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
