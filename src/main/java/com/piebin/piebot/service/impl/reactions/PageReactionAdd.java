package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.service.PageService;
import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.utility.EmojiManager;
import com.piebin.piebot.utility.ReactionManager;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

@AllArgsConstructor
public class PageReactionAdd implements PieReactionAdd {
    private final Sentence sentence;
    private final PageService pageService;

    @Override
    public void execute(MessageReactionAddEvent event) {
        User user = event.getUser();
        MessageReaction reaction = event.getReaction();
        reaction.removeReaction(user).queue();

        MessageEmbed embed = ReactionManager.getEmbed(event);
        if (embed == null)
            return;
        int page = 1;
        try {
            Integer.parseInt(embed.getTitle().replace(sentence + " - ", ""));
        } catch (Exception e) {}

        Emoji emoji = reaction.getEmoji();
        page += EmojiManager.getPageCount(emoji);

        Message message = event.retrieveMessage().complete();
        message.editMessageEmbeds(pageService.getPage(page).build()).queue();
    }
}
