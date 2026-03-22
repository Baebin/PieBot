package com.piebin.piebot.controller;

import com.piebin.piebot.component.DiscordBotInfo;
import com.piebin.piebot.component.DiscordJDA;
import com.piebin.piebot.listener.CommandListener;
import com.piebin.piebot.listener.ReactionListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DiscordBotController implements ApplicationRunner {
    private static final String API = "/api/bot/";

    private static boolean status = false;

    private final DiscordJDA discordJDA;
    private final DiscordBotInfo botInfo;

    private final CommandListener commandListener;
    private final ReactionListener reactionListener;

    @GetMapping(API + "run")
    public ResponseEntity<Boolean> run() {
        if (status)
            return ResponseEntity.ok(false);
        JDA jda = JDABuilder.createDefault(botInfo.getToken())
                .setActivity(Activity.playing("ㅋ 도움말"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(
                        commandListener,
                        reactionListener
                )
                .build();
        discordJDA.setJda(jda);
        /*
        jda.updateCommands().addCommands(
                SlashCommand.TEST.getData()
        ).queue();
        */

        status = true;
        log.info("Discord Bot Started");
        return ResponseEntity.ok(true);
    }

    @GetMapping(API + "stop")
    public ResponseEntity<Boolean> stop() {
        if (!status)
            return ResponseEntity.ok(false);
        discordJDA.getJda().shutdown();
        status = false;
        log.info("Discord Bot Stopped");
        return ResponseEntity.ok(true);
    }

    @Override
    public void run(ApplicationArguments args) {
        run();
    }
}
