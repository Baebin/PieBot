package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.service.PageService;
import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.utility.EmojiManager;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

@AllArgsConstructor
public class NumberPageReactionAdd implements PieReactionAdd {
    private final PageService pageService;

    @Override
    public void execute(MessageReactionAddEvent event) {
        User user = event.getUser();
        MessageReaction reaction = event.getReaction();
        reaction.removeReaction(user).queue();

        Emoji emoji = reaction.getEmoji();
        int page = EmojiManager.getNumber(emoji);
        if (page == 0)
            return;
        Message message = event.retrieveMessage().complete();
        EmbedBuilder embedBuilder = pageService.getPage(page);
        if (embedBuilder == null)
            return;
        MessageEmbed embed = embedBuilder.build();
        message.editMessageEmbeds(embed).queue();
    }
}
