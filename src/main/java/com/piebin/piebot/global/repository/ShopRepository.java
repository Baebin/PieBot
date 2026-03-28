package com.piebin.piebot.global.repository;

import com.piebin.piebot.global.domain.Shop;
import com.piebin.piebot.global.entity.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByIdx(Long idx);

    List<Shop> findAllByItemCategory(ItemCategory itemCategory);
}
