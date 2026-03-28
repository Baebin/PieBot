package com.piebin.piebot.global.repository;

import com.piebin.piebot.global.domain.Account;
import com.piebin.piebot.global.domain.EasterEgg;
import com.piebin.piebot.global.domain.EasterEggHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EasterEggHistoryRepository extends JpaRepository<EasterEggHistory, Long> {
    boolean existsByEasterEgg(EasterEgg easterEgg);
    boolean existsByAccountAndEasterEgg(Account account, EasterEgg easterEgg);

    List<EasterEggHistory> findAllByIsFirst(boolean isFirst);
}
