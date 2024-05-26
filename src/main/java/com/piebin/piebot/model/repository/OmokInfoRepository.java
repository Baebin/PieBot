package com.piebin.piebot.model.repository;

import com.piebin.piebot.model.domain.OmokInfo;
import com.piebin.piebot.model.domain.OmokRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OmokInfoRepository extends JpaRepository<OmokInfo, Long> {
    void deleteAllByRoom(OmokRoom omokRoom);

    Optional<OmokInfo> findByRoomAndPosition(OmokRoom room, String position);
}
