package com.piebin.piebot.service;

import net.dv8tion.jda.api.EmbedBuilder;

public interface PageService {
    EmbedBuilder getPage(int page);
}
