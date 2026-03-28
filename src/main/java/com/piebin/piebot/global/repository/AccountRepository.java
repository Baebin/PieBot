package com.piebin.piebot.global.repository;

import com.piebin.piebot.global.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsById(String id);

    Optional<Account> findById(String id);

}
