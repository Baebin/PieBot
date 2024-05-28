package com.piebin.piebot.model.repository;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByAccount(Account account);
}
