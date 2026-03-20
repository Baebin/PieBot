package com.piebin.piebot.model.repository;

import com.piebin.piebot.model.domain.Yacht;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YachtRepository extends JpaRepository<Yacht, Long> {
}
