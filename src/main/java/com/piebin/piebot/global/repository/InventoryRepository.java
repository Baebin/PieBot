package com.piebin.piebot.global.repository;

import com.piebin.piebot.global.domain.Account;
import com.piebin.piebot.global.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    boolean existsByAccount(Account account);

    Optional<Inventory> findByAccount(Account account);
}
