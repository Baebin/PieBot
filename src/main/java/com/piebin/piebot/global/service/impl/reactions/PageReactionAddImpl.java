package com.piebin.piebot.global.service.impl.reactions;

import com.piebin.piebot.global.entity.Sentence;
import com.piebin.piebot.global.service.PageReactionAdd;
import com.piebin.piebot.global.service.PageService;
import com.piebin.piebot.global.utility.EmojiManager;
import com.piebin.piebot.global.utility.MessageRetriever;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PageReactionAddImpl implements PageReactionAdd {
    private final MessageRetriever messageRetriever;

    @Override
    public void execute(Sentence sentence, PageService pageService, MessageReactionAddEvent event) {
        User user = event.getUser();
        MessageReaction reaction = event.getReaction();
        reaction.removeReaction(user).queue();

        messageRetriever.retrieveMessage(event, (message) -> {
            Optional<MessageEmbed> optionalMessageEmbed = messageRetriever.getEmbed(message);
            if (optionalMessageEmbed.isEmpty())
                return;
            MessageEmbed embed = optionalMessageEmbed.get();

            int page = 1;
            try {
                page = Integer.parseInt(embed.getTitle().replace(sentence + " - ", ""));
            } catch (Exception e) {}

            Emoji emoji = reaction.getEmoji();
            page += EmojiManager.getPageCount(emoji);

            message.editMessageEmbeds(pageService.getPage(page).build()).queue();
        });
    }
}
