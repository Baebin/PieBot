package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.impl.commands.EasterEggListCommand;
import com.piebin.piebot.utility.EmojiManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EasterEggListReactionAdd implements PieReactionAdd {
    private final EasterEggListCommand easterEggListCommand;

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
        MessageEmbed embed = easterEggListCommand.getPage(page).build();
        message.editMessageEmbeds(embed).queue();
    }
}
