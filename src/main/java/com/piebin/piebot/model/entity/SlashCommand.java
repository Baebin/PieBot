package com.piebin.piebot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

@Getter
@AllArgsConstructor
public enum SlashCommand {
    TEST("test", "테스트");

    public final String name;
    public final String description;

    public SlashCommandData getData() {
        return Commands.slash(name, description);
    }
}
