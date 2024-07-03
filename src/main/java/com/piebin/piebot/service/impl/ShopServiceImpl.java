package com.piebin.piebot.service.impl;

import com.piebin.piebot.exception.ShopException;
import com.piebin.piebot.exception.entity.ShopErrorCode;
import com.piebin.piebot.model.domain.*;
import com.piebin.piebot.model.dto.shop.ShopItemDto;
import com.piebin.piebot.model.repository.InventoryRepository;
import com.piebin.piebot.model.repository.ItemRepository;
import com.piebin.piebot.model.repository.ShopHistoryRepository;
import com.piebin.piebot.model.repository.ShopRepository;
import com.piebin.piebot.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;
    private final ShopRepository shopRepository;
    private final ShopHistoryRepository shopHistoryRepository;

    void validateDayCountLimit(Account account, Shop shop, long amount) {
        Long countLimit = shop.getDay_count_limit();
        if (countLimit == null)
            return;
        LocalDate localDate = LocalDate.now();
        Long cnt = shopHistoryRepository.countByAccountAndItemInfoAndRegDateAfter(account, shop.getItemInfo(), localDate);
        if (cnt + amount > countLimit)
            throw new ShopException(ShopErrorCode.DAY_COUNT_LIMIT);
    }

    void validateWeekCountLimit(Account account, Shop shop, long amount) {
        Long countLimit = shop.getWeek_count_limit();
        if (countLimit == null)
            return;
        LocalDate localDate = LocalDate.now();
        localDate.minusDays(localDate.getDayOfWeek().getValue() - 1);
        Long cnt = shopHistoryRepository.countByAccountAndItemInfoAndRegDateAfter(account, shop.getItemInfo(), localDate);
        if (cnt + amount > countLimit)
            throw new ShopException(ShopErrorCode.DAY_COUNT_LIMIT);
    }

    void validateMonthCountLimit(Account account, Shop shop, long amount) {
        Long countLimit = shop.getMonth_count_limit();
        if (countLimit == null)
            return;
        LocalDate localDate = LocalDate.now().withDayOfMonth(1);
        Long cnt = shopHistoryRepository.countByAccountAndItemInfoAndRegDateAfter(account, shop.getItemInfo(), localDate);
        if (cnt + amount > countLimit)
            throw new ShopException(ShopErrorCode.MONTH_COUNT_LIMIT);
    }

    void validateTotalCountLimit(Account account, Shop shop, long amount) {
        Long countLimit = shop.getTotal_count_limit();
        if (countLimit == null)
            return;
        Long cnt = shopHistoryRepository.countByAccountAndItemInfo(account, shop.getItemInfo());
        if (cnt + amount > countLimit)
            throw new ShopException(ShopErrorCode.TOTAL_COUNT_LIMIT);
    }

    void saveShopHistory(Account account, ItemInfo itemInfo, long amount) {
        ShopHistory shopHistory = ShopHistory.builder()
                .account(account)
                .itemInfo(itemInfo)
                .amount(amount)
                .build();
        shopHistoryRepository.save(shopHistory);
    }

    @Override
    @Transactional(noRollbackFor = ShopException.class)
    public void buyItem(Account account, ShopItemDto dto) {
        Shop shop = shopRepository.findByIdx(dto.getIdx())
                .orElseThrow(() -> new ShopException(ShopErrorCode.NOT_FOUND));
        // Count Validation
        validateTotalCountLimit(account, shop, dto.getAmount());
        validateMonthCountLimit(account, shop, dto.getAmount());
        validateWeekCountLimit(account, shop, dto.getAmount());
        validateDayCountLimit(account, shop, dto.getAmount());
        // Money Check
        long price = shop.getPrice() * dto.getAmount();
        if (account.getMoney() < price)
            throw new ShopException(ShopErrorCode.MONEY_LESS);
        // Inventory Check
        Inventory inventory = inventoryRepository.findByAccount(account)
                .orElseThrow(() -> new ShopException(ShopErrorCode.INVENTORY_NOT_FOUND));
        ItemInfo itemInfo = shop.getItemInfo();
        Item item = inventory.findItem(itemInfo);
        if (item != null) {
            if (item.getCount() + dto.getAmount() > itemInfo.getMaxStackCount())
                throw new ShopException(ShopErrorCode.MAX_STACK_COUNT_LIMIT);
            item.setCount(item.getCount() + dto.getAmount());
        } else {
            if (dto.getAmount() > itemInfo.getMaxStackCount())
                throw new ShopException(ShopErrorCode.MAX_STACK_COUNT_LIMIT);
            item = Item.builder()
                    .itemInfo(itemInfo)
                    .count(dto.getAmount())
                    .build();
            itemRepository.save(item);
            inventory.addItem(item);
        }
        account.setMoney(account.getMoney() - price);
        saveShopHistory(account, itemInfo, dto.getAmount());
    }
}
