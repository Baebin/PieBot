package com.piebin.piebot.service.impl;

import com.piebin.piebot.component.DiscordJDA;
import com.piebin.piebot.factory.YachtCommandFactory;
import com.piebin.piebot.model.domain.*;
import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.entity.EmbedSentence;
import com.piebin.piebot.model.entity.UniEmoji;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.model.repository.YachtRepository;
import com.piebin.piebot.model.repository.YachtRoomRepository;
import com.piebin.piebot.service.YachtDrawingService;
import com.piebin.piebot.service.YachtService;
import com.piebin.piebot.utility.*;
import kotlin.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class YachtServiceImpl implements YachtService {
    private final int BONUS_SCORE = 35;
    private final int BONUS_NEED_SCORE = 63;
    private final int SMALL_STRAIGHT_SCORE = 15;
    private final int LARGE_STRAIGHT_SCORE = 30;
    private final int YACHT_SCORE = 50;

    private final DiscordJDA discordJDA;

    private final AccountRepository accountRepository;
    private final YachtRepository yachtRepository;
    private final YachtRoomRepository yachtRoomRepository;

    private final YachtCommandFactory yachtCommandFactory;

    private final YachtDrawingService yachtDrawingService;

    @Override
    public String getBoardString(YachtRoom yachtRoom) {
        List<String> lines = new ArrayList<>();
        lines.add(
                "## " + MessageManager.getMention(yachtRoom.getAccount().getId())
                        + " vs " + MessageManager.getMention(yachtRoom.getOpponent().getId())
        );
        lines.add("> 현재 차례: "
                + MessageManager.getMention((yachtRoom.getTurnCount() % 2 == 0 ? yachtRoom.getAccount() : yachtRoom.getOpponent()).getId())
                + " **(" + yachtRoom.getRollCount() + "/3)**"
        );
        lines.add("> *ex) z 1, z 3, z 5, z 포커, z 풀하우스, etc.");

        String board = String.join("\n", lines);
        return board;
    }

    @Override
    @Transactional(readOnly = true)
    public Message sendYachtRoomMessage(MessageChannel channel, YachtRoom yachtRoom) {
        FileUpload fileUpload;
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
    public void editYachtRoomMessage(YachtRoom yachtRoom) {
        CompletableFuture.runAsync(() -> {
            FileUpload fileUpload ;
            try {
                fileUpload = FileUpload.fromData(yachtDrawingService.getBoard(yachtRoom));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Optional<Message> optionalMessage = discordJDA.getMessageByID(yachtRoom.getChannelId(), yachtRoom.getMessageId());
            if (optionalMessage.isEmpty())
                return;
            Message message = optionalMessage.get();
            message.editMessage(getBoardString(yachtRoom)).setFiles(fileUpload).complete();
        });
    }

    /*
    Reaction Add Event
    */

    private Optional<YachtRoom> getYachtRoomIfMyTurn(String id) {
        // YachtRoom Exists?
        if (!yachtRoomRepository.existsByAccount_IdOrOpponent_Id(id, id))
            return Optional.empty();
        log.info("Yacht Room Exists");

        // YachtRoom
        Optional<YachtRoom> optionalYachtRoom = yachtRoomRepository.findByAccount_IdOrOpponent_Id(id, id);
        if (optionalYachtRoom.isEmpty())
            return Optional.empty();
        YachtRoom yachtRoom = optionalYachtRoom.get();
        log.info("Yacht Room Found");

        // Turn Check
        if (yachtRoom.getTurnCount() % 2 == 0 && yachtRoom.getOpponent().getId().equals(id))
            return Optional.empty();
        if (yachtRoom.getTurnCount() % 2 != 0 && yachtRoom.getAccount().getId().equals(id))
            return Optional.empty();
        log.info("Yacht Room Turn Checked");

        return Optional.of(yachtRoom);
    }

    @Override
    @Transactional
    public void selectEmoji(MessageReactionAddEvent event) {
        String id = event.getUserId();
        Emoji emoji = event.getEmoji();
        log.info("id: {}, emoji: {}", id, emoji);

        Optional<YachtRoom> optionalYachtRoom = getYachtRoomIfMyTurn(id);
        if (optionalYachtRoom.isEmpty())
            return;
        YachtRoom yachtRoom = optionalYachtRoom.get();

        if (emoji.equals(UniEmoji.RECYCLE.getEmoji())) {
            if (rollDices(yachtRoom))
                editYachtRoomMessage(yachtRoom);
        } else if (emoji.equals(UniEmoji.SMALL_RED_TRIANGLE.getEmoji())) {
            yachtRoom.setIsHoldingDice(true);
        } else if (emoji.equals(UniEmoji.SMALL_RED_TRIANGLE_DOWN.getEmoji())) {
            yachtRoom.setIsHoldingDice(false);
        } else {
            int number = EmojiManager.getNumber(emoji);
            if (number < 1 || number > 5)
                return;
            if (yachtRoom.getIsHoldingDice()) {
                if (!selectDice(yachtRoom, number))
                    return;
            }
            else if (!deselectDice(yachtRoom, number))
                return;
            editYachtRoomMessage(yachtRoom);
        }

    }

    @Override
    @Transactional
    public void select(MessageReceivedEvent event, String type) {
        String id = event.getAuthor().getId();
        log.info("id: {}, type: {}", id, type);

        Optional<YachtRoom> optionalYachtRoom = getYachtRoomIfMyTurn(id);
        if (optionalYachtRoom.isEmpty())
            return;
        YachtRoom yachtRoom = optionalYachtRoom.get();

        // Roll Check
        if (yachtRoom.getRollCount() == 0)
            return;
        log.info("Yacht Room Roll Count >= 1");

        // 1 ~ 6
        for (int i = 1; i <= 6; i++) {
            if (!type.equals(i + ""))
                continue;
            log.info("command found: {}", i);

            if (selectNumberScore(yachtRoom, i)) {
                yachtRoom.nextTurn();

                // Discord Message
                event.getMessage().delete().queue();
                editYachtRoomMessage(yachtRoom);
            }
            return;
        }

        Map<List<String>, Predicate<YachtRoom>> commandMap = Map.of(
                yachtCommandFactory.getChoiceCommands(), this::selectChoiceScore,
                yachtCommandFactory.getFourOfAKindCommands(), this::selectFourOfAKindScore,
                yachtCommandFactory.getFullHouseCommands(), this::selectFullHouseScore,
                yachtCommandFactory.getSmallStraightCommands(), this::selectSmallStraightScore,
                yachtCommandFactory.getLargeStraightCommands(), this::selectLargeStraightScore,
                yachtCommandFactory.getYachtCommands(), this::selectYachtScore
        );
        for (Map.Entry<List<String>, Predicate<YachtRoom>> entry : commandMap.entrySet()) {
            if (entry.getKey().stream().anyMatch(c -> c.equalsIgnoreCase(type))) {
                log.info("command found: {}", entry.getKey());

                if (entry.getValue().test(yachtRoom)) {
                    yachtRoom.nextTurn();

                    // Discord Message
                    event.getMessage().delete().queue();
                    editYachtRoomMessage(yachtRoom);
                }
                return;
            }
        }
    }

    /*
    Score
    */

    private YachtScoreBoard getScoreBoard(YachtRoom yachtRoom) {
        return (yachtRoom.getTurnCount() % 2 == 0 ? yachtRoom.getAccountScoreBoard() : yachtRoom.getOpponentScoreBoard());
    }

    private int getNumberScore(YachtRoom yachtRoom, int number) {
        int cnt = 0;
        for (int dice : yachtRoom.getDices())
            if (dice == number)
                cnt++;
        return (cnt * number);
    }

    private Pair<Integer, List<Integer>> getSumAndCnts(YachtRoom yachtRoom) {
        int sum = 0;
        List<Integer> cnts = new ArrayList<>(Collections.nCopies(6, 0));
        for (int dice : yachtRoom.getDices()) {
            cnts.set(dice - 1, cnts.get(dice - 1) + 1);
            sum += dice;
        }
        return new Pair<>(sum, cnts);
    }

    @Override
    @Transactional
    public boolean selectNumberScore(YachtRoom yachtRoom, int number) {
        YachtScoreBoard scoreBoard = getScoreBoard(yachtRoom);
        Map<Integer, Supplier<Integer>> getters = Map.of(
                1, scoreBoard::getAces,
                2, scoreBoard::getDeuces,
                3, scoreBoard::getThrees,
                4, scoreBoard::getFours,
                5, scoreBoard::getFives,
                6, scoreBoard::getSixes
        );
        Map<Integer, Consumer<Integer>> setters = Map.of(
                1, scoreBoard::setAces,
                2, scoreBoard::setDeuces,
                3, scoreBoard::setThrees,
                4, scoreBoard::setFours,
                5, scoreBoard::setFives,
                6, scoreBoard::setSixes
        );
        if (getters.get(number).get() != null)
            return false;
        setters.get(number).accept(getNumberScore(yachtRoom, number));
        selectBonusScore(yachtRoom);
        return true;
    }

    @Override
    @Transactional
    public boolean selectBonusScore(YachtRoom yachtRoom) {
        YachtScoreBoard scoreBoard = getScoreBoard(yachtRoom);
        if (scoreBoard.getBonus() != null)
            return false;
        int sum = 0;
        for (Integer score : scoreBoard.getNumberScores()) {
            if (score == null)
                return false;
            sum += score;
        }
        scoreBoard.setBonus(sum >= BONUS_NEED_SCORE ? BONUS_SCORE : 0);
        return true;
    }

    @Override
    @Transactional
    public boolean selectChoiceScore(YachtRoom yachtRoom) {
        YachtScoreBoard scoreBoard = getScoreBoard(yachtRoom);
        if (scoreBoard.getChoice() != null)
            return false;
        int score = 0;
        for (int dice : yachtRoom.getDices())
            score += dice;
        scoreBoard.setChoice(score);
        return true;
    }

    @Override
    @Transactional
    public boolean selectFourOfAKindScore(YachtRoom yachtRoom) {
        YachtScoreBoard scoreBoard = getScoreBoard(yachtRoom);
        if (scoreBoard.getFourOfAKind() != null)
            return false;

        // Score & Count
        Pair<Integer, List<Integer>> p = getSumAndCnts(yachtRoom);

        // Count Check
        boolean hasFourOfAKind = false;
        for (int cnt : p.getSecond()) {
            if (cnt < 4)
                continue;
            hasFourOfAKind = true;
            break;
        }
        scoreBoard.setFourOfAKind(hasFourOfAKind ? p.getFirst() : 0);
        return true;
    }

    @Override
    @Transactional
    public boolean selectFullHouseScore(YachtRoom yachtRoom) {
        YachtScoreBoard scoreBoard = getScoreBoard(yachtRoom);
        if (scoreBoard.getFullHouse() != null)
            return false;

        // Score & Count
        Pair<Integer, List<Integer>> p = getSumAndCnts(yachtRoom);

        // Count Check
        boolean hasTwo = false, hasThree = false;
        for (int cnt : p.getSecond()) {
            if (cnt == 2)
                hasTwo = true;
            else if (cnt == 3)
                hasThree = true;
        }
        scoreBoard.setFullHouse((hasTwo && hasThree) ? p.component1() : 0);
        return true;
    }

    @Override
    @Transactional
    public boolean selectSmallStraightScore(YachtRoom yachtRoom) {
        YachtScoreBoard scoreBoard = getScoreBoard(yachtRoom);
        if (scoreBoard.getSmallStraight() != null)
            return false;

        // Count
        List<Integer> cnts = getSumAndCnts(yachtRoom).component2();

        // Count Check
        int straightCnt = 0;
        for (int cnt : cnts) {
            straightCnt = (cnt == 0 ? 0 : straightCnt + 1);
            if (straightCnt >= 4) break;
        }
        scoreBoard.setSmallStraight((straightCnt >= 4) ? SMALL_STRAIGHT_SCORE : 0);
        return true;
    }

    @Override
    @Transactional
    public boolean selectLargeStraightScore(YachtRoom yachtRoom) {
        YachtScoreBoard scoreBoard = getScoreBoard(yachtRoom);
        if (scoreBoard.getLargeStraight() != null)
            return false;

        // Count
        List<Integer> cnts = getSumAndCnts(yachtRoom).component2();

        // Count Check
        int straightCnt = 0;
        for (int cnt : cnts) {
            straightCnt = (cnt == 0 ? 0 : straightCnt + 1);
            if (straightCnt >= 5) break;
        }
        scoreBoard.setLargeStraight((straightCnt >= 5) ? LARGE_STRAIGHT_SCORE : 0);
        return true;
    }

    @Override
    @Transactional
    public boolean selectYachtScore(YachtRoom yachtRoom) {
        YachtScoreBoard scoreBoard = getScoreBoard(yachtRoom);
        if (scoreBoard.getYacht() != null)
            return false;

        // Count
        List<Integer> cnts = getSumAndCnts(yachtRoom).component2();

        // Count Check
        boolean hasYacht = false;
        for (int cnt : cnts) {
            if (cnt < 5)
                continue;
            hasYacht = true;
            break;
        }
        scoreBoard.setYacht(hasYacht ? YACHT_SCORE : 0);
        return true;
    }

    @Override
    @Transactional
    public boolean selectDice(YachtRoom yachtRoom, int number) {
        if (number > yachtRoom.getNonSelectedDices().size())
            return false;
        yachtRoom.getSelectedDices().add(yachtRoom.getNonSelectedDices().get(number - 1));
        yachtRoom.getNonSelectedDices().remove(number - 1);
        return true;
    }

    @Override
    @Transactional
    public boolean deselectDice(YachtRoom yachtRoom, int number) {
        if (number > yachtRoom.getSelectedDices().size())
            return false;
        yachtRoom.getNonSelectedDices().add(yachtRoom.getSelectedDices().get(number - 1));
        yachtRoom.getSelectedDices().remove(number - 1);
        return true;
    }

    @Override
    @Transactional
    public boolean rollDices(YachtRoom yachtRoom) {
        if (!yachtRoom.canRoll())
            return false;
        yachtRoom.setNonSelectedDices(
                (yachtRoom.getRollCount() == 0) ?
                        IntStream.range(0, 5).mapToObj(dice -> RandomManager.nextInt(1, 6)).toList()
                        : yachtRoom.getNonSelectedDices().stream().map(dice -> RandomManager.nextInt(1, 6)).toList());
        yachtRoom.increaseRollCount();
        return true;
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

        // Set Message Info
        Message sendMessage = sendYachtRoomMessage(event.getChannel(), yachtRoom);
        yachtRoom.setChannelId(sendMessage.getChannelId());
        yachtRoom.setMessageId(sendMessage.getId());
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
        YachtRoom yachtRoom = optionalYachtRoom.get();
        // Set Message Info
        Message sendMessage = sendYachtRoomMessage(event.getChannel(), yachtRoom);
        yachtRoom.setChannelId(sendMessage.getChannelId());
        yachtRoom.setMessageId(sendMessage.getId());
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
