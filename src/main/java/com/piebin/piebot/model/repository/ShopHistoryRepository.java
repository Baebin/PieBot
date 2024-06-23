package com.piebin.piebot.model.repository;

import com.piebin.piebot.model.domain.ShopHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopHistoryRepository extends JpaRepository<ShopHistory, Long> {
}
