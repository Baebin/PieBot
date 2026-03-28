package com.piebin.piebot.global.service;

import com.piebin.piebot.global.domain.Account;
import com.piebin.piebot.global.dto.shop.ShopItemDto;

public interface ShopService {
    void buyItem(Account account, ShopItemDto dto);
}
