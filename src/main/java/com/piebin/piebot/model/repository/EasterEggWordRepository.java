package com.piebin.piebot.model.repository;

import com.piebin.piebot.model.domain.EasterEggWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EasterEggWordRepository extends JpaRepository<EasterEggWord, Long> {
    @Query(value = "select ew from EasterEggWord ew where lower(:word) like concat('%', ew.word, '%')")
    List<EasterEggWord> findByWordIgnoreCase(String word);
}
