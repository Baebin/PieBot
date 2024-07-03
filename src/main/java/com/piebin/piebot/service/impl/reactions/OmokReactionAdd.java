package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.Omok;
import com.piebin.piebot.model.domain.OmokRoom;
import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.entity.EmbedSentence;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.model.repository.OmokRepository;
import com.piebin.piebot.model.repository.OmokRoomRepository;
import com.piebin.piebot.service.OmokSkinService;
import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.impl.commands.OmokCommand;
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
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OmokReactionAdd implements PieReactionAdd {
    private final AccountRepository accountRepository;
    private final OmokRepository omokRepository;
    private final OmokRoomRepository omokRoomRepository;

    private final OmokCommand omokCommand;

    private final OmokSkinService omokSkinService;

    @Override
    @Transactional
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
        boolean existsFrom = omokRoomRepository.existsByAccountOrOpponent(from, from);
        if (existsFrom) {
            MessageEmbed embed = EmbedMessageHelper.getEmbedBuilder(CommandSentence.OMOK_PVP_EXISTS_TO, Color.RED).build();
            message.editMessageEmbeds(embed).queue();
            return;
        }
        Account to = optionalTo.get();
        boolean existsTo = omokRoomRepository.existsByAccountOrOpponent(to, to);
        if (existsTo) {
            MessageEmbed embed = EmbedMessageHelper.getEmbedBuilder(CommandSentence.OMOK_PVP_EXISTS_FROM, Color.RED).build();
            message.editMessageEmbeds(embed).queue();
            return;
        }

        MessageEmbed embed = EmbedMessageHelper.getEmbedBuilder(EmbedSentence.OMOK_PVP_STARTED, Color.GREEN).build();
        message.editMessageEmbeds(embed).queue();

        Optional<Omok> optionalOmokFrom = omokRepository.findByAccount(from);
        OmokRoom omokRoom = OmokRoom.builder()
                .account(from)
                .opponent(to)
                .omokSkin((optionalOmokFrom.isPresent() ? optionalOmokFrom.get().getOmokSkin() : null))
                .build();
        omokRoomRepository.save(omokRoom);

        String board = omokCommand.createBoard(omokRoom);
        Message boardMessage;
        if (omokRoom.getOmokSkin() != null) {
            FileUpload fileUpload = FileUpload.fromData(omokSkinService.getBoard(omokRoom));
            boardMessage = event.getChannel().sendMessage(board).setFiles(fileUpload).complete();
        } else boardMessage = event.getChannel().sendMessage(board).complete();
        omokRoom.setMessageId(boardMessage.getId());
    }
}
