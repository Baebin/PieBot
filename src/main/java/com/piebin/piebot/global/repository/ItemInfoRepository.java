package com.piebin.piebot.global.repository;

import com.piebin.piebot.global.domain.ItemInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemInfoRepository extends JpaRepository<ItemInfo, Long> {
}
