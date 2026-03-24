package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.service.NumberPageReactionAdd;
import com.piebin.piebot.service.PageService;
import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.utility.EmojiManager;
import com.piebin.piebot.utility.MessageRetriever;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NumberPageReactionAddImpl implements NumberPageReactionAdd {
    private final MessageRetriever messageRetriever;

    @Override
    public void execute(PageService pageService, MessageReactionAddEvent event) {
        User user = event.getUser();
        MessageReaction reaction = event.getReaction();
        reaction.removeReaction(user).queue();

        Emoji emoji = reaction.getEmoji();
        int page = EmojiManager.getNumber(emoji);
        if (page == 0)
            return;
        messageRetriever.retrieveMessage(event, (message -> {
            EmbedBuilder embedBuilder = pageService.getPage(page);
            if (embedBuilder == null)
                return;
            MessageEmbed embed = embedBuilder.build();
            message.editMessageEmbeds(embed).queue();
        }));
    }
}
