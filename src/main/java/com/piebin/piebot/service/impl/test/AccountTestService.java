package com.piebin.piebot.service.impl.test;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.Inventory;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.model.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class AccountTestService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    @Rollback(value = false)
    @Transactional
    void initInventories() {
        List<Account> accounts = accountRepository.findAll();
        List<Inventory> inventories = new ArrayList<>();
        for (Account account : accounts) {
            if (inventoryRepository.existsByAccount(account))
                continue;
            Inventory inventory = Inventory.builder()
                    .account(account)
                    .build();
            inventories.add(inventory);
        }
        inventoryRepository.saveAll(inventories);
    }
}
