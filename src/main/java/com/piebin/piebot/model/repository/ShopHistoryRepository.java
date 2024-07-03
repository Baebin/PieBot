package com.piebin.piebot.model.repository;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.ItemInfo;
import com.piebin.piebot.model.domain.ShopHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShopHistoryRepository extends JpaRepository<ShopHistory, Long> {
    Long countByItemInfo(ItemInfo itemInfo);
    Long countByAccountAndItemInfo(Account account, ItemInfo itemInfo);
    Long countByAccountAndItemInfoAndRegDateAfter(Account account, ItemInfo itemInfo, LocalDateTime localDateTime);
}
