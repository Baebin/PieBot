package com.piebin.piebot.model.repository;

import com.piebin.piebot.model.domain.EasterEgg;
import com.piebin.piebot.model.entity.CommandSentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EasterEggRepository extends JpaRepository<EasterEgg, Long> {
    boolean existsBySentence(CommandSentence sentence);
}
