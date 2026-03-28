package com.piebin.piebot.yacht.repository;

import com.piebin.piebot.global.domain.Account;
import com.piebin.piebot.yacht.domain.Yacht;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface YachtRepository extends JpaRepository<Yacht, Long> {
    Optional<Yacht> findByAccount(Account account);
}
