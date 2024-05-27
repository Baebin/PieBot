package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.impl.commands.PatchNoteCommand;
import com.piebin.piebot.utility.EmojiManager;
import com.piebin.piebot.utility.ReactionManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatchNoteReactionAdd implements PieReactionAdd {
    private final PatchNoteCommand command;

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
            Integer.parseInt(embed.getTitle().replace(Sentence.PATCH_NOTE + " - ", ""));
        } catch (Exception e) {}

        Emoji emoji = reaction.getEmoji();
        page += EmojiManager.getPageCount(emoji);

        Message message = event.retrieveMessage().complete();
        message.editMessageEmbeds(command.getPage(page).build()).queue();
    }
}
