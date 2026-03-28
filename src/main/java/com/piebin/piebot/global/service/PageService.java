package com.piebin.piebot.global.service;

import net.dv8tion.jda.api.EmbedBuilder;

public interface PageService {
    EmbedBuilder getPage(int page);
}
