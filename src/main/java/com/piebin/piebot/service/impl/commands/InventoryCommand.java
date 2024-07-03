package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.exception.AccountException;
import com.piebin.piebot.exception.entity.AccountErrorCode;
import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.Inventory;
import com.piebin.piebot.model.domain.Item;
import com.piebin.piebot.model.entity.EmbedSentence;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.model.repository.InventoryRepository;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.utility.EmbedMessageHelper;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryCommand implements PieCommand {
    private final AccountRepository accountRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional(readOnly = true)
    public void execute(MessageReceivedEvent event) {
        try {
            Account account = accountRepository.findById(event.getAuthor().getId())
                    .orElseThrow(() -> new AccountException(AccountErrorCode.NOT_FOUND));
            Inventory inventory = inventoryRepository.findByAccount(account)
                    .orElseThrow(() -> new AccountException(AccountErrorCode.INVENTORY_NOT_FOUND));

            List<Item> items = inventory.getItems();
            items.sort((a, b) -> {
                if (a.getItemInfo().getName().equals(b.getItemInfo().getName()))
                    return 0;
                return (a.getItemInfo().getName().compareTo(b.getItemInfo().getName()) < 0 ? -1 : 1);
            });

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(Sentence.INVENTORY.getMessage());
            embedBuilder.setColor(Color.GREEN);

            int i = 0;
            for (Item item : items) {
                String description = (++i) + ". "
                        + item.getItemInfo().getName()
                        + " (x" + item.getCount() + ")";
                embedBuilder.appendDescription(description);
            }
            event.getMessage().replyEmbeds(embedBuilder.build()).queue();
        } catch (AccountException e) {
            switch (e.getErrorCode()) {
                case NOT_FOUND:
                    EmbedMessageHelper.replyEmbedMessage(event.getMessage(), EmbedSentence.PROFILE_NOT_FOUND, Color.RED);
                    break;
                case INVENTORY_NOT_FOUND:
                    EmbedMessageHelper.replyEmbedMessage(event.getMessage(), EmbedSentence.INVENTORY_NOT_FOUND, Color.RED);
                    break;
                default:
                    EmbedMessageHelper.replyEmbedMessage(event.getMessage(), EmbedSentence.SYSTEM_ERROR, Color.RED);
                    break;
            };
        } catch (Exception e) {
            EmbedMessageHelper.replyEmbedMessage(event.getMessage(), EmbedSentence.SYSTEM_ERROR, Color.RED);
        }
    }
}
