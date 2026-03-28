package com.piebin.piebot.omok.repository;

import com.piebin.piebot.global.domain.Account;
import com.piebin.piebot.omok.domain.Omok;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OmokRepository extends JpaRepository<Omok, Long> {
    Optional<Omok> findByAccount(Account account);
}
