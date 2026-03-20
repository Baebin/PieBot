package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.Omok;
import com.piebin.piebot.model.domain.OmokRoom;
import com.piebin.piebot.model.domain.YachtRoom;
import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.entity.EmbedSentence;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.model.repository.YachtRoomRepository;
import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.YachtService;
import com.piebin.piebot.service.impl.commands.YachtCommand;
import com.piebin.piebot.utility.EmbedMessageHelper;
import com.piebin.piebot.utility.ReactionManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YachtReactionAdd implements PieReactionAdd {
    private final AccountRepository accountRepository;
    private final YachtRoomRepository yachtRoomRepository;

    private final YachtCommand yachtCommand;

    private final YachtService yachtService;

    @Override
    public void execute(MessageReactionAddEvent event) {
        User user = event.getUser();
        MessageReaction reaction = event.getReaction();
        reaction.removeReaction(user).queue();

        Message message = ReactionManager.getMessage(event);
        if (message == null)
            return;
        Message rMessage = message.getReferencedMessage();
        if (rMessage == null)
            return;
        if (!rMessage.getMentions().isMentioned(event.getUser()))
            return;

        Optional<Account> optionalFrom = accountRepository.findById(rMessage.getAuthor().getId());
        Optional<Account> optionalTo = accountRepository.findById(event.getUserId());
        if (optionalFrom.isEmpty() || optionalTo.isEmpty())
            return;

        Account from = optionalFrom.get();
        boolean existsFrom = yachtRoomRepository.existsByAccountOrOpponent(from, from);
        if (existsFrom) {
            MessageEmbed embed = EmbedMessageHelper.getEmbedBuilder(CommandSentence.YACHT_PVP_EXISTS_TO, Color.RED).build();
            message.editMessageEmbeds(embed).queue();
            return;
        }
        Account to = optionalTo.get();
        boolean existsTo = yachtRoomRepository.existsByAccountOrOpponent(to, to);
        if (existsTo) {
            MessageEmbed embed = EmbedMessageHelper.getEmbedBuilder(CommandSentence.YACHT_PVP_EXISTS_FROM, Color.RED).build();
            message.editMessageEmbeds(embed).queue();
            return;
        }

        MessageEmbed embed = EmbedMessageHelper.getEmbedBuilder(EmbedSentence.YACHT_PVP_STARTED, Color.GREEN).build();
        message.editMessageEmbeds(embed).queue();

        FileUpload fileUpload = FileUpload.fromData(yachtService.getBoard(new YachtRoom()));
        event.getChannel().sendMessage("Test").setFiles(fileUpload).complete();
    }
}
