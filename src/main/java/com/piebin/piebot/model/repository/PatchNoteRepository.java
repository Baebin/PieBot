package com.piebin.piebot.model.repository;

import com.piebin.piebot.model.domain.PatchNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface PatchNoteRepository extends JpaRepository<PatchNote, Long> {
    Page<PatchNote> findAll(Pageable pageable);
}
