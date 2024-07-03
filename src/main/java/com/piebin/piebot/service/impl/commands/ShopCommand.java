package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.exception.AccountException;
import com.piebin.piebot.exception.ShopException;
import com.piebin.piebot.exception.entity.AccountErrorCode;
import com.piebin.piebot.exception.entity.ShopErrorCode;
import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.Shop;
import com.piebin.piebot.model.dto.embed.EmbedDto;
import com.piebin.piebot.model.dto.shop.ShopItemDto;
import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.entity.EmbedSentence;
import com.piebin.piebot.model.entity.ItemCategory;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.model.repository.ShopRepository;
import com.piebin.piebot.service.PageService;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.service.ShopService;
import com.piebin.piebot.utility.*;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopCommand implements PieCommand, PageService {
    public static int PAGES = 3;

    private final AccountRepository accountRepository;

    private final ShopService shopService;
    private final ShopRepository shopRepository;

    List<String> getShopLines(Shop shop) {
        List<String> lines = new ArrayList<>();
        lines.add("- 가격: " + NumberManager.getNumber(shop.getPrice()) + "빙");
        if (shop.getDayCountLImit() != null)
            lines.add("- 하루 구매 제한: " + NumberManager.getNumber(shop.getDayCountLImit()) + "회");
        if (shop.getWeekCountLimit() != null)
            lines.add("- 주간 구매 제한: " + NumberManager.getNumber(shop.getWeekCountLimit()) + "회");
        if (shop.getMonthCountLimit() != null)
            lines.add("- 한달 구매 제한: " + NumberManager.getNumber(shop.getMonthCountLimit()) + "회");
        if (shop.getAccountCountLimit() != null)
            lines.add("- 계정 구매 제한: " + NumberManager.getNumber(shop.getAccountCountLimit()) + "회");
        if (shop.getTotalCountLimit() != null)
            lines.add("- 전체 구매 제한: " + NumberManager.getNumber(shop.getTotalCountLimit()) + "회");
        return lines;
    }

    private void addField(EmbedBuilder embedBuilder, Shop shop) {
        List<String> lines = getShopLines(shop);
        String description = String.join("\n", lines);
        embedBuilder.addField(shop.getIdx() + ". " + shop.getItemInfo().getName(), description, false);
    }

    private EmbedBuilder getInfo(Shop shop) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(shop.getItemInfo().getName());
        embedBuilder.setColor(Color.GREEN);

        List<String> lines = new ArrayList<>();
        lines.add("- 판매 번호: " + NumberManager.getNumber(shop.getIdx()) + "번");
        lines.add("- 아이템 번호: " + NumberManager.getNumber(shop.getItemInfo().getIdx()) + "번");

        String desIdx = String.join("\n", lines);
        String desShop = String.join("\n", getShopLines(shop));
        String desItem = shop.getItemInfo().getDescription();

        embedBuilder.addField("고유 번호", desIdx, false);
        embedBuilder.addField("판매 정보", desShop, false);
        embedBuilder.addField("상품 정보", desItem, false);
        return embedBuilder;
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent event) {
        List<String> args = CommandManager.getArgs(event);
        if (args.size() >= 3) {
            if (args.get(2).equals("목록") || args.get(2).equalsIgnoreCase("list")) {
                int initPage = 1;
                if (args.size() >= 4)
                    initPage = PageManager.getPage(PAGES, args.get(3));
                TextChannel channel = event.getChannel().asTextChannel();
                Message message = channel.sendMessageEmbeds(getPage(initPage).build()).complete();
                for (int i = 1; i <= PAGES; i++)
                    message.addReaction(EmojiManager.getEmoji(i)).queue();
                return;
            } else if (args.get(2).equals("정보") || args.get(2).equalsIgnoreCase("info")) {
                try {
                    long idx = Long.parseLong(args.get(3));
                    Shop shop = shopRepository.findByIdx(idx)
                            .orElseThrow(() -> new ShopException(ShopErrorCode.NOT_FOUND));
                    EmbedBuilder embedBuilder = getInfo(shop);
                    event.getMessage().replyEmbeds(embedBuilder.build()).queue();
                    return;
                } catch (Exception e) {}
                EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.SHOP_IDX_NOT_FOUND);
                return;
            } else if (args.get(2).equals("구매") || args.get(2).equalsIgnoreCase("buy")) {
                try {
                    long idx = Long.parseLong(args.get(3));
                    long amount;
                    try {
                        amount = Long.parseLong(args.get(4));
                    } catch (Exception e) {
                        amount = 1;
                    }
                    if (amount < 1) {
                        EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.SHOP_AMOUNT_INVALID);
                        return;
                    }
                    Account account = accountRepository.findById(event.getAuthor().getId())
                            .orElseThrow(() -> new AccountException(AccountErrorCode.NOT_FOUND));
                    ShopItemDto shopItemDto = ShopItemDto.builder()
                            .idx(idx)
                            .amount(amount)
                            .build();
                    shopService.buyItem(account, shopItemDto);
                    EmbedMessageHelper.replyCommandMessage(event.getMessage(), CommandSentence.SHOP_BUY_COMPLETED, Color.GREEN);
                    return;
                } catch (ShopException e) {
                    switch (e.getErrorCode()) {
                        case NOT_FOUND:
                            EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.SHOP_IDX_NOT_FOUND);
                            break;
                        case INVENTORY_NOT_FOUND:
                            EmbedMessageHelper.replyEmbedMessage(event.getMessage(), EmbedSentence.INVENTORY_NOT_FOUND, Color.RED);
                            break;
                        case MAX_STACK_COUNT_LIMIT:
                            EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.SHOP_MAX_STACK_COUNT_LIMIT);
                            break;
                        case DAY_COUNT_LIMIT:
                            EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.SHOP_DAY_COUNT_LIMIT);
                            break;
                        case WEEK_COUNT_LIMIT:
                            EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.SHOP_WEEK_COUNT_LIMIT);
                            break;
                        case MONTH_COUNT_LIMIT:
                            EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.SHOP_MONTH_COUNT_LIMIT);
                            break;
                        case ACCOUNT_COUNT_LIMIT:
                            EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.SHOP_ACCOUNT_COUNT_LIMIT);
                            break;
                        case TOTAL_COUNT_LIMIT:
                            EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.SHOP_TOTAL_COUNT_LIMIT);
                            break;
                        case MONEY_LESS:
                            Optional<Account> optionalAccount = accountRepository.findById(event.getAuthor().getId());
                            if (optionalAccount.isPresent()) {
                                Account account = optionalAccount.get();

                                EmbedDto dto = new EmbedDto(CommandSentence.SHOP_MONEY_LESS, Color.RED);
                                dto.changeDescription(NumberManager.getNumber(account.getMoney()));
                                EmbedMessageHelper.replyEmbedMessage(event.getMessage(), dto);
                            } else EmbedMessageHelper.replyEmbedMessage(event.getMessage(), EmbedSentence.PROFILE_NOT_FOUND, Color.RED);
                            break;
                    }
                } catch (Exception e) {
                    EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.SHOP_IDX_NOT_FOUND);
                }
                return;
            }
        }
        EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.SHOP_ARG1);
    }

    @Override
    @Transactional(readOnly = true)
    public EmbedBuilder getPage(int page) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GREEN);

        List<Shop> shops;
        switch (page) {
            case 1:
                embedBuilder.setTitle(Sentence.SHOP_DEFAULT.getMessage());
                shops = shopRepository.findAllByItemCategory(ItemCategory.DEFAULT);
                break;
            case 2:
                embedBuilder.setTitle(Sentence.SHOP_GAME.getMessage());
                shops = shopRepository.findAllByItemCategory(ItemCategory.GAME);
                break;
            case 3:
                embedBuilder.setTitle(Sentence.SHOP_ETC.getMessage());
                shops = shopRepository.findAllByItemCategory(ItemCategory.ETC);
                break;
            default:
                shops = new ArrayList<>();
                break;
        }
        shops.sort((a, b) -> {
            if (a.getPrice().equals(b.getPrice()))
                return 0;
            return (a.getPrice() < b.getPrice() ? -1 : 1);
        });
        for (Shop shop : shops)
            addField(embedBuilder, shop);
        return embedBuilder;
    }
}
