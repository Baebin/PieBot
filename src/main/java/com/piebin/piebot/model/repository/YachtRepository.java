package com.piebin.piebot.model.repository;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.Yacht;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface YachtRepository extends JpaRepository<Yacht, Long> {
    Optional<Yacht> findByAccount(Account account);
}
