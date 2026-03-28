package com.piebin.piebot.global.repository;

import com.piebin.piebot.global.domain.EasterEgg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EasterEggRepository extends JpaRepository<EasterEgg, Long> {
}
