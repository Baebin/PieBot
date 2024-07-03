package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.exception.ShopException;
import com.piebin.piebot.exception.entity.ShopErrorCode;
import com.piebin.piebot.model.domain.Shop;
import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.entity.ItemCategory;
import com.piebin.piebot.model.entity.Sentence;
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

@Service
@RequiredArgsConstructor
public class ShopCommand implements PieCommand, PageService {
    public static int PAGES = 1;

    private final ShopService shopService;
    private final ShopRepository shopRepository;

    private void addField(EmbedBuilder embedBuilder, Shop shop) {
        List<String> lines = new ArrayList<>();
        lines.add("- 가격: " + NumberManager.getNumber(shop.getPrice()) + "빙");
        if (shop.getDay_count_limit() != null)
            lines.add("- 하루 구매 제한: " + NumberManager.getNumber(shop.getDay_count_limit()) + "회");
        if (shop.getWeek_count_limit() != null)
            lines.add("- 주간 구매 제한: " + NumberManager.getNumber(shop.getWeek_count_limit()) + "회");
        if (shop.getMonth_count_limit() != null)
            lines.add("- 한달 구매 제한: " + NumberManager.getNumber(shop.getMonth_count_limit()) + "회");
        if (shop.getTotal_count_limit() != null)
            lines.add("- 계정 구매 제한: " + NumberManager.getNumber(shop.getTotal_count_limit()) + "회");
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

        lines.clear();
        lines.add("- 가격: " + NumberManager.getNumber(shop.getPrice()) + "빙");
        if (shop.getDay_count_limit() != null)
            lines.add("- 일별 구매 제한: " + NumberManager.getNumber(shop.getDay_count_limit()) + "회");
        if (shop.getWeek_count_limit() != null)
            lines.add("- 주별 구매 제한: " + NumberManager.getNumber(shop.getWeek_count_limit()) + "회");
        if (shop.getMonth_count_limit() != null)
            lines.add("- 월별 구매 제한: " + NumberManager.getNumber(shop.getMonth_count_limit()) + "회");
        if (shop.getTotal_count_limit() != null)
            lines.add("- 계정 구매 제한: " + NumberManager.getNumber(shop.getTotal_count_limit()) + "회");
        String desShop = String.join("\n", lines);
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
                EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.SHOP_ARG2_IDX_NOT_FOUND);
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

        switch (page) {
            case 1:
                embedBuilder.setTitle(Sentence.SHOP_DEFAULT.getMessage());
                List<Shop> shops = shopRepository.findAllByItemCategory(ItemCategory.DEFAULT);
                shops.sort((a, b) -> {
                    if (a.getPrice().equals(b.getPrice()))
                        return 0;
                    return (a.getPrice() < b.getPrice() ? -1 : 1);
                });
                for (Shop shop : shops)
                    addField(embedBuilder, shop);
                break;
        }
        return embedBuilder;
    }
}
