package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.domain.*;
import com.piebin.piebot.model.dto.embed.EmbedDto;
import com.piebin.piebot.model.entity.*;
import com.piebin.piebot.model.repository.*;
import com.piebin.piebot.service.OmokSkinService;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.service.OmokService;
import com.piebin.piebot.utility.*;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OmokCommand implements PieCommand, OmokService {
    public static final int DEFAULT_SIZE = 14;
    public static final int MAXIMUM_SIZE = 21;
    public static final int SKIN_AURORA_SIZE = 21;

    private final AccountRepository accountRepository;
    private final InventoryRepository inventoryRepository;

    private final OmokRepository omokRepository;
    private final OmokRoomRepository omokRoomRepository;
    private final OmokInfoRepository omokInfoRepository;

    private final OmokSkinService omokSkinService;

    private final OmokRankCommand omokRankCommand;

    @Override
    @Transactional
    public EmbedBuilder getProfile(Account account) {
        Optional<Omok> optionalOmok = omokRepository.findByAccount(account);

        Omok omok;
        if (optionalOmok.isEmpty()) {
            omok = Omok.builder()
                    .account(account)
                    .build();
            omokRepository.save(omok);
        }
        else omok = optionalOmok.get();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Sentence.PROFILE.getMessage());
        embedBuilder.setColor(Color.GREEN);

        long total = (omok.getWin() + omok.getTie() + omok.getLose());
        double odds = 0.0;
        if (total != 0)
            odds = (100 * omok.getWin() / total);
        embedBuilder.addField("이름", account.getName(), false);
        embedBuilder.addField("승률", String.format("%.2f", odds) + "%", false);
        String value = NumberManager.getNumber(omok.getWin()) + "승 "
                + NumberManager.getNumber(omok.getTie()) + "무"
                + NumberManager.getNumber(omok.getLose()) + "패";
        embedBuilder.addField("전적", value, false);

        return embedBuilder;
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent event) {
        List<String> args = CommandManager.getArgs(event);
        if (args.size() >= 3) {
            if (args.get(2).equals("대전") || args.get(2).equalsIgnoreCase("pvp")) {
                if (args.size() >= 4) {
                    String userId = CommandManager.getMentionId(args.get(3));
                    if (event.getAuthor().getId().equals(userId)) {
                        EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.OMOK_PVP_SELF);
                        return;
                    }
                    Optional<Account> optionalFrom = accountRepository.findById(event.getAuthor().getId());
                    Optional<Account> optionalTo = accountRepository.findById(userId);
                    if (optionalFrom.isPresent() && optionalTo.isPresent()) {
                        Account from = optionalFrom.get();
                        boolean existsFrom = omokRoomRepository.existsByAccountOrOpponent(from, from);
                        if (existsFrom) {
                            EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.OMOK_PVP_EXISTS_FROM);
                            return;
                        }
                        Account to = optionalTo.get();
                        boolean existsTo = omokRoomRepository.existsByAccountOrOpponent(to, to);
                        if (existsTo) {
                            EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.OMOK_PVP_EXISTS_TO);
                            return;
                        }
                        Message message = EmbedMessageHelper.replyCommandMessage(event.getMessage(), CommandSentence.OMOK_PVP, Color.GREEN);
                        message.addReaction(UniEmoji.CHECK.getEmoji()).queue();
                        return;
                    }
                }
                EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.OMOK_PVP_ARG2);
                return;
            }
            else if (args.get(2).equals("퇴장") || args.get(2).equalsIgnoreCase("quit")) {
                Optional<Account> optionalAccount = accountRepository.findById(event.getAuthor().getId());
                if (optionalAccount.isEmpty())
                    return;
                Account account = optionalAccount.get();
                Optional<OmokRoom> optionalOmokRoom = omokRoomRepository.findByAccountOrOpponent(account, account);
                if (optionalOmokRoom.isEmpty()) {
                    EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.OMOK_QUIT_NONE);
                    return;
                }
                OmokRoom omokRoom = optionalOmokRoom.get();
                EmbedMessageHelper.replyCommandMessage(event.getMessage(), CommandSentence.OMOK_QUIT, Color.GREEN);

                addTie(omokRoom.getAccount());
                addTie(omokRoom.getOpponent());

                omokInfoRepository.deleteAllByRoom(omokRoom);
                omokRoomRepository.delete(omokRoom);
                return;
            }
            else if (args.get(2).equals("순위") || args.get(2).equalsIgnoreCase("rank")) {
                omokRankCommand.execute(event);
                return;
            }
            else if (args.get(2).equals("프로필") || args.get(2).equalsIgnoreCase("profile")) {
                Optional<Account> optionalAccount = accountRepository.findById(event.getAuthor().getId());
                if (optionalAccount.isEmpty())
                    return;
                FileUpload fileUpload = MessageManager.getProfile(event.getAuthor().getAvatarUrl());
                if (fileUpload != null) {
                    Account account = optionalAccount.get();
                    event.getMessage().replyFiles(fileUpload)
                            .setEmbeds(getProfile(account).build())
                            .queue();
                } else EmbedMessageHelper.replyEmbedMessage(event.getMessage(), EmbedSentence.PROFILE_NOT_FOUND, Color.RED);
                return;
            }
            else if (args.get(2).equals("이어하기") || args.get(2).equalsIgnoreCase("continue")) {
                Optional<Account> optionalAccount = accountRepository.findById(event.getAuthor().getId());
                if (optionalAccount.isEmpty())
                    return;
                Account account = optionalAccount.get();
                Optional<OmokRoom> optionalOmokRoom = omokRoomRepository.findByAccountOrOpponent(account, account);
                if (optionalOmokRoom.isEmpty()) {
                    EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.OMOK_CONTINUE_NONE);
                    return;
                }
                OmokRoom omokRoom = optionalOmokRoom.get();
                String board = createBoard(omokRoom);
                Message boardMessage;
                if (omokRoom.getOmokSkin() != null) {
                    FileUpload fileUpload = FileUpload.fromData(omokSkinService.getBoard(omokRoom));
                    boardMessage = event.getChannel().sendMessage(board).setFiles(fileUpload).complete();
                } else boardMessage = event.getChannel().sendMessage(board).complete();
                omokRoom.setMessageId(boardMessage.getId());
                return;
            }
            else if (args.get(2).equals("스킨") || args.get(2).equalsIgnoreCase("skin")) {
                Optional<Account> optionalAccount = accountRepository.findById(event.getAuthor().getId());
                if (optionalAccount.isEmpty()) {
                    EmbedMessageHelper.replyEmbedMessage(event.getMessage(), EmbedSentence.PROFILE_NOT_FOUND, Color.RED);
                    return;
                }
                Optional<Inventory> optionalInventory = inventoryRepository.findByAccount(optionalAccount.get());
                if (optionalInventory.isEmpty()) {
                    EmbedMessageHelper.replyEmbedMessage(event.getMessage(), EmbedSentence.INVENTORY_NOT_FOUND, Color.RED);
                    return;
                }
                Account account = optionalAccount.get();
                Optional<Omok> optionalOmok = omokRepository.findByAccount(account);
                if (optionalOmok.isEmpty()) {
                    EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.OMOK_NOT_FOUND);
                    return;
                }
                Omok omok = optionalOmok.get();
                if (args.size() >= 4) {
                    if (args.get(3).equals("해제")) {
                        if (omok.getOmokSkin() == null) {
                            EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.OMOK_SKIN_ALREADY_REMOVED);
                            return;
                        }
                        EmbedDto dto = new EmbedDto(CommandSentence.OMOK_SKIN_REMOVED, Color.GREEN);
                        dto.changeDescription(omok.getOmokSkin().name());
                        EmbedMessageHelper.replyEmbedMessage(event.getMessage(), dto);

                        omok.setOmokSkin(null);
                        return;
                    } else if (args.get(3).equalsIgnoreCase(OmokSkin.AURORA.name())) {
                        Inventory inventory = optionalInventory.get();
                        if (!inventory.hasOmokSkin(OmokSkin.AURORA)) {
                            EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.OMOK_SKIN_NONE);
                            return;
                        }
                        EmbedDto dto = new EmbedDto(CommandSentence.OMOK_SKIN_SELECTED, Color.GREEN);
                        dto.changeDescription(OmokSkin.AURORA.name());
                        EmbedMessageHelper.replyEmbedMessage(event.getMessage(), dto);

                        omok.setOmokSkin(OmokSkin.AURORA);
                        return;
                    }
                }
                Inventory inventory = optionalInventory.get();
                List<String> skins = new ArrayList<>();
                if (inventory.hasOmokSkin(OmokSkin.AURORA))
                    skins.add("AURORA");
                EmbedDto dto = new EmbedDto(CommandSentence.OMOK_SKIN_INVALID, Color.RED);
                dto.changeDescription(
                        omok.getOmokSkin() == null ? "없음" : omok.getOmokSkin().name(),
                        skins.isEmpty() ? "없음" : String.join(",", skins)
                );
                EmbedMessageHelper.replyEmbedMessage(event.getMessage(), dto);
                return;
            }
        }
        EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.OMOK_ARG1);
    }

    @Override
    @Transactional(readOnly = true)
    public String createBoard(OmokRoom omokRoom) {
        List<String> lines = new ArrayList<>();
        lines.add(
                "## " + MessageManager.getMention(omokRoom.getAccount().getId())
                + " vs " + MessageManager.getMention(omokRoom.getOpponent().getId())
        );
        if (omokRoom.getOmokSkin() == null) {
            lines.add("┍┭┭┭┭┭┭┭┭┭┭┭┭┑ " + EmojiManager.getUniCircle(1));
            for (int i = 2; i <= (DEFAULT_SIZE - 1); i++)
                lines.add("┝┿┿┿┿┿┿┿┿┿┿┿┿┥ " + EmojiManager.getUniCircle(i));
            lines.add("┕┷┷┷┷┷┷┷┷┷┷┷┷┙ " + EmojiManager.getUniCircle(DEFAULT_SIZE));
            for (int y = 1; y <= DEFAULT_SIZE; y++) {
                StringBuilder builder = new StringBuilder(lines.get(y));
                for (char x = 'A'; x <= 'A' + (DEFAULT_SIZE - 1); x++) {
                    Optional<OmokInfo> optionalOmokInfo = omokInfoRepository.findByRoomAndPosition(omokRoom, "" + x + y);
                    if (optionalOmokInfo.isEmpty())
                        continue;
                    OmokInfo omokInfo = optionalOmokInfo.get();
                    if (omokInfo.getState() == OmokState.BLACK)
                        builder.setCharAt((x - 'A'), '○');
                    else builder.setCharAt((x - 'A'), '●');
                }
                lines.set(y, builder.toString());
            }
            char x = 'a';
            String bottom = "";
            for (int i = 1; i <= DEFAULT_SIZE; i++)
                bottom += EmojiManager.getUniAlphabet((x++));
            lines.add(bottom);
            lines.add("");
        }
        OmokState state = omokRoom.getState();
        if (state == OmokState.BLACK)
            lines.add("> 현재 차례: " + MessageManager.getMention(omokRoom.getAccount().getId()));
        else lines.add("> 현재 차례: " + MessageManager.getMention(omokRoom.getOpponent().getId()));
        lines.add("> *ex) z A1, z B5, z P9*");

        String board = String.join("\n", lines);
        return board;
    }

    @Override
    @Transactional
    public void addWin(Account account) {
        Optional<Omok> optionalOmok = omokRepository.findByAccount(account);
        if (optionalOmok.isEmpty()) {
            Omok omok = Omok.builder()
                    .account(account)
                    .win(1L)
                    .build();
            omokRepository.save(omok);
        } else {
            Omok omok = optionalOmok.get();
            omok.setWin(omok.getWin() + 1);
        }
        account.setMoney(account.getMoney() + 1000);
    }

    @Override
    @Transactional
    public void addTie(Account account) {
        Optional<Omok> optionalOmok = omokRepository.findByAccount(account);
        if (optionalOmok.isEmpty()) {
            Omok omok = Omok.builder()
                    .account(account)
                    .tie(1L)
                    .build();
            omokRepository.save(omok);
        } else {
            Omok omok = optionalOmok.get();
            omok.setTie(omok.getTie() + 1);
        }
    }

    @Override
    @Transactional
    public void addLose(Account account) {
        Optional<Omok> optionalOmok = omokRepository.findByAccount(account);
        if (optionalOmok.isEmpty()) {
            Omok omok = Omok.builder()
                    .account(account)
                    .lose(1L)
                    .build();
            omokRepository.save(omok);
        } else {
            Omok omok = optionalOmok.get();
            omok.setLose(omok.getLose() + 1);
        }
        account.setMoney(account.getMoney() + 500);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isWin(OmokRoom omokRoom, OmokState state, char x, int y) {
        // Left&Right, Up&Down, Diagonal&Left, Diagonal&Right
        int[] cnt = { 1, 1, 1, 1 };
        int[] dx = { -1, 1, 0, 0, -1, 1, 1, -1  };
        int[] dy = { 0, 0, -1, 1, -1, 1, -1, 1 };

        int size = (omokRoom.getOmokSkin() != null ? SKIN_AURORA_SIZE : DEFAULT_SIZE);
        for (int d = 0; d < 8; d++) {
            char X = x;
            int Y = y;
            while (true) {
                X += dx[d];
                Y += dy[d];
                if (!(('A' <= X && X <= 'A' + (size - 1)) && (1 <= Y && Y <= size)))
                    break;
                Optional<OmokInfo> optional = omokInfoRepository.findByRoomAndPosition(omokRoom, "" + X + Y);
                if (optional.isEmpty())
                    break;
                if (optional.get().getState() != state)
                    break;
                cnt[d / 2]++;
            }
        }
        for (int i = 0; i < 4; i++)
            if (cnt[i] >= 5)
                return true;
        return false;
    }

    @Override
    @Transactional
    public void selectPosition(MessageReceivedEvent event, Account account, char x, int y) {
        Optional<OmokRoom> optionalOmokRoom = omokRoomRepository.findByAccountOrOpponent(account, account);
        if (optionalOmokRoom.isEmpty())
            return;
        OmokRoom omokRoom = optionalOmokRoom.get();

        // Board Validation
        if (omokRoom.getOmokSkin() == null && (x > ('A' + (DEFAULT_SIZE - 1)) || y > DEFAULT_SIZE))
            return;

        Message message;
        TextChannel channel = event.getChannel().asTextChannel();
        try {
            message = channel.retrieveMessageById(omokRoom.getMessageId()).complete();
        } catch (Exception e) {
            return;
        }
        event.getMessage().delete().queue();

        boolean isBlack = omokRoom.getAccount().getId() == account.getId();
        if ((isBlack && omokRoom.getState() != OmokState.BLACK) || (!isBlack && omokRoom.getState() != OmokState.WHITE))
            return;
        String position = "" + x + y;
        Optional<OmokInfo> optionalOmokInfo =  omokInfoRepository.findByRoomAndPosition(omokRoom, position);
        if (optionalOmokInfo.isPresent())
            return;

        // Win Check
        OmokState state = (isBlack ? OmokState.BLACK : OmokState.WHITE);
        OmokInfo omokInfo = OmokInfo.builder()
                .room(omokRoom)
                .state(state)
                .position(position)
                .build();
        omokInfoRepository.save(omokInfo);
        omokRoom.setState((isBlack ? OmokState.WHITE : OmokState.BLACK));

        if (omokRoom.getOmokSkin() != null) {
            omokSkinService.updateBoard(omokRoom, state, x, y);
            FileUpload fileUpload = FileUpload.fromData(omokSkinService.getBoard(omokRoom));
            message.editMessage(createBoard(omokRoom)).setFiles(fileUpload).queue();
        } else message.editMessage(createBoard(omokRoom)).queue();

        if (!isWin(omokRoom, state, x, y))
            return;
        Account winner = account;
        Account loser;
        if (winner != omokRoom.getAccount())
            loser = omokRoom.getAccount();
        else loser = omokRoom.getOpponent();

        addWin(winner);
        addLose(loser);

        omokInfoRepository.deleteAllByRoom(omokRoom);
        omokRoomRepository.delete(omokRoom);

        EmbedDto dto = new EmbedDto(EmbedSentence.OMOK_PVP_WIN, Color.GREEN);
        dto.changeMessage(account.getName());
        EmbedMessageHelper.replyEmbedMessage(message, dto);
    }
}
