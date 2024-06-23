package com.piebin.piebot.model.repository;

import com.piebin.piebot.model.domain.ItemInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemInfoRepository extends JpaRepository<ItemInfo, Long> {
}
