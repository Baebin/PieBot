package com.piebin.piebot.yacht.repository;

import com.piebin.piebot.global.domain.Account;
import com.piebin.piebot.yacht.domain.YachtRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface YachtRoomRepository extends JpaRepository<YachtRoom, Long> {
    boolean existsByAccountOrOpponent(Account account, Account opponent);
    boolean existsByAccount_IdOrOpponent_Id(String accountId, String opponentId);
    boolean existsByAccount_Id(String accountId);

    Optional<YachtRoom> findByIdx(Long idx);
    Optional<YachtRoom> findByAccountOrOpponent(Account account, Account opponent);
    Optional<YachtRoom> findByAccount_IdOrOpponent_Id(String accountId, String opponentId);
}
