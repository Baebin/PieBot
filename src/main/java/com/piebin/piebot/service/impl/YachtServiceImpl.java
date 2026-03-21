package com.piebin.piebot.service.impl;

import com.piebin.piebot.model.domain.*;
import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.entity.EmbedSentence;
import com.piebin.piebot.model.entity.UniEmoji;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.model.repository.YachtRepository;
import com.piebin.piebot.model.repository.YachtRoomRepository;
import com.piebin.piebot.service.ImageService;
import com.piebin.piebot.service.YachtDrawingService;
import com.piebin.piebot.service.YachtService;
import com.piebin.piebot.utility.*;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YachtServiceImpl implements YachtService {
    private static final String BOARD = "board";

    private final ImageService imageService;
    private final YachtDrawingService yachtDrawingService;

    private final AccountRepository accountRepository;
    private final YachtRepository yachtRepository;
    private final YachtRoomRepository yachtRoomRepository;

    @Override
    public String getBoardString(YachtRoom yachtRoom) {
        List<String> lines = new ArrayList<>();
        lines.add(
                "## " + MessageManager.getMention(yachtRoom.getAccount().getId())
                        + " vs " + MessageManager.getMention(yachtRoom.getOpponent().getId())
        );
        lines.add("> 현재 차례: " + MessageManager.getMention((yachtRoom.getTurnCount() == 0 ? yachtRoom.getAccount() : yachtRoom.getOpponent()).getId()));
        lines.add("> *ex) z 1, z 3, z 5, z 포커, z 풀하우스, etc.");

        String board = String.join("\n", lines);
        return board;
    }

    @Override
    public Message sendYachtRoomMessage(MessageChannelUnion channel, YachtRoom yachtRoom) {
        FileUpload fileUpload = null;
        try {
            fileUpload = FileUpload.fromData(yachtDrawingService.getBoard(yachtRoom));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Message message = channel.sendMessage(getBoardString(yachtRoom)).setFiles(fileUpload).complete();

        message.addReaction(UniEmoji.SMALL_RED_TRIANGLE.getEmoji()).complete();
        message.addReaction(UniEmoji.SMALL_RED_TRIANGLE_DOWN.getEmoji()).complete();
        message.addReaction(UniEmoji.RECYCLE.getEmoji()).complete();
        for (int i = 1; i <= 5; i++)
            message.addReaction(EmojiManager.getEmoji(i)).complete();
        return message;
    }

    @Override
    @Transactional(readOnly = true)
    public void invitePVP(MessageReceivedEvent event) {
        List<String> args = CommandManager.getArgs(event);
        if (args.size() >= 4) {
            String userId = CommandManager.getMentionId(args.get(3));
            if (event.getAuthor().getId().equals(userId)) {
                EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.YACHT_PVP_SELF);
                return;
            }
            Optional<Account> optionalFrom = accountRepository.findById(event.getAuthor().getId());
            Optional<Account> optionalTo = accountRepository.findById(userId);
            if (optionalFrom.isPresent() && optionalTo.isPresent()) {
                Account from = optionalFrom.get();
                boolean existsFrom = yachtRoomRepository.existsByAccountOrOpponent(from, from);
                if (existsFrom) {
                    EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.YACHT_PVP_EXISTS_FROM);
                    return;
                }
                Account to = optionalTo.get();
                boolean existsTo = yachtRoomRepository.existsByAccountOrOpponent(to, to);
                if (existsTo) {
                    EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.YACHT_PVP_EXISTS_TO);
                    return;
                }
                Message message = EmbedMessageHelper.replyCommandMessage(event.getMessage(), CommandSentence.YACHT_PVP, Color.GREEN);
                message.addReaction(UniEmoji.CHECK.getEmoji()).queue();
                return;
            }
        }
        EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.YACHT_PVP_ARG2);
    }

    @Override
    @Transactional
    public void createYachtRoom(MessageReactionAddEvent event) {
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

        // Accepted Message
        MessageEmbed embed = EmbedMessageHelper.getEmbedBuilder(EmbedSentence.YACHT_PVP_STARTED, Color.GREEN).build();
        message.editMessageEmbeds(embed).queue();

        // Create Game Room
        YachtRoom yachtRoom = YachtRoom.builder()
                .account(from)
                .opponent(to)
                .build();
        yachtRoomRepository.save(yachtRoom);

        // Set Message Id
        yachtRoom.setMessageId(sendYachtRoomMessage(event.getChannel(), yachtRoom).getId());
    }


    @Override
    @Transactional
    public void quitYachtRoom(MessageReceivedEvent event) {
        Optional<Account> optionalAccount = accountRepository.findById(event.getAuthor().getId());
        if (optionalAccount.isEmpty())
            return;
        Account account = optionalAccount.get();
        Optional<YachtRoom> optionalYachtRoom = yachtRoomRepository.findByAccountOrOpponent(account, account);
        if (optionalYachtRoom.isEmpty()) {
            EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.YACHT_QUIT_NONE);
            return;
        }
        YachtRoom yachtRoom = optionalYachtRoom.get();
        EmbedMessageHelper.replyCommandMessage(event.getMessage(), CommandSentence.YACHT_QUIT, Color.GREEN);

        addTie(yachtRoom.getAccount());
        addTie(yachtRoom.getOpponent());

        yachtRoomRepository.delete(yachtRoom);
    }

    @Override
    @Transactional
    public void continueYachtRoom(MessageReceivedEvent event) {
        Optional<Account> optionalAccount = accountRepository.findById(event.getAuthor().getId());
        if (optionalAccount.isEmpty())
            return;
        Account account = optionalAccount.get();
        Optional<YachtRoom> optionalYachtRoom = yachtRoomRepository.findByAccountOrOpponent(account, account);
        if (optionalYachtRoom.isEmpty()) {
            EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.YACHT_CONTINUE_NONE);
            return;
        }
        // Set Message Id
        YachtRoom yachtRoom = optionalYachtRoom.get();
        yachtRoom.setMessageId(sendYachtRoomMessage(event.getChannel(), yachtRoom).getId());
    }

    @Override
    @Transactional
    public Yacht getOrCreateYacht(Account account) {
        Optional<Yacht> optionalYacht = yachtRepository.findByAccount(account);
        if (optionalYacht.isEmpty()) {
            Yacht yacht = Yacht.builder()
                    .account(account)
                    .build();
            yachtRepository.save(yacht);
            return yacht;
        }
        return optionalYacht.get();
    }

    @Override
    @Transactional
    public void addWin(Account account) {
        Yacht yacht = getOrCreateYacht(account);
        yacht.setWin(yacht.getWin() + 1);
    }

    @Override
    @Transactional
    public void addTie(Account account) {
        Yacht yacht = getOrCreateYacht(account);
        yacht.setTie(yacht.getTie() + 1);
    }

    @Override
    @Transactional
    public void addLose(Account account) {
        Yacht yacht = getOrCreateYacht(account);
        yacht.setLose(yacht.getLose() + 1);
    }
}
