package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.impl.commands.HelpCommand;
import com.piebin.piebot.utility.EmojiManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
public class HelpReactionAdd implements PieReactionAdd {
    @Override
    public void execute(MessageReactionAddEvent event) {
        User user = event.getUser();
        MessageReaction reaction = event.getReaction();
        reaction.removeReaction(user).queue();

        MessageEmbed embed = null;
        Emoji emoji = reaction.getEmoji();
        HelpCommand command = new HelpCommand();
        for (int i = 1; i <= HelpCommand.PAGES; i++)
            if (emoji.equals(EmojiManager.getEmoji(i)))
                embed = command.getPage(i).build();
        if (embed == null)
            return;
        Message message = event.retrieveMessage().complete();
        message.editMessageEmbeds(embed).queue();
    }
}
