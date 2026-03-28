package com.piebin.piebot.omok.repository;

import com.piebin.piebot.global.domain.Account;
import com.piebin.piebot.omok.domain.OmokRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OmokRoomRepository extends JpaRepository<OmokRoom, Long> {
    boolean existsByAccountOrOpponent(Account account, Account opponent);

    Optional<OmokRoom> findByIdx(Long idx);
    Optional<OmokRoom> findByAccountOrOpponent(Account account, Account opponent);
}
