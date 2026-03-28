package com.piebin.piebot.global.repository;

import com.piebin.piebot.global.domain.Account;
import com.piebin.piebot.global.domain.ItemInfo;
import com.piebin.piebot.global.domain.ShopHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShopHistoryRepository extends JpaRepository<ShopHistory, Long> {
    Long countByItemInfo(ItemInfo itemInfo);
    Long countByAccountAndItemInfo(Account account, ItemInfo itemInfo);
    Long countByAccountAndItemInfoAndRegDateAfter(Account account, ItemInfo itemInfo, LocalDateTime localDateTime);
}
