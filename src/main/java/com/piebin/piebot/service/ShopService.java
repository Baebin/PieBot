package com.piebin.piebot.service;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.dto.shop.ShopItemDto;

public interface ShopService {
    void buyItem(Account account, ShopItemDto dto);
}
