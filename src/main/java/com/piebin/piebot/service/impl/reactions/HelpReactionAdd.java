package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.model.entity.UniEmoji;
import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.impl.commands.HelpCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class HelpReactionAdd implements PieReactionAdd {
    @Override
    public void execute(MessageReactionAddEvent event) {
        User user = event.getUser();
        MessageReaction reaction = event.getReaction();
        reaction.removeReaction(user).queue();

        MessageEmbed embed = null;
        Emoji emoji = reaction.getEmoji();
        HelpCommand command = new HelpCommand();
        if (emoji.equals(UniEmoji.NUM_1.getEmoji()))
            embed = command.getPage(1).build();
        else if (emoji.equals(UniEmoji.NUM_2.getEmoji()))
            embed = command.getPage(2).build();
        else if (emoji.equals(UniEmoji.NUM_3.getEmoji()))
            embed = command.getPage(3).build();
        if (embed == null)
            return;
        Message message = event.retrieveMessage().complete();
        message.editMessageEmbeds(embed).queue();
    }
}
