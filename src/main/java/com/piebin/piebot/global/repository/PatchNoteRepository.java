package com.piebin.piebot.global.repository;

import com.piebin.piebot.global.domain.PatchNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface PatchNoteRepository extends JpaRepository<PatchNote, Long> {
    Page<PatchNote> findAll(Pageable pageable);
}
