package com.piebin.piebot.model.repository;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.OmokRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OmokRoomRepository extends JpaRepository<OmokRoom, Long> {
    boolean existsByAccountOrOpponent(Account account, Account opponent);

    Optional<OmokRoom> findByAccountOrOpponent(Account account, Account opponent);
}
