package com.piebin.piebot.omok.repository;

import com.piebin.piebot.omok.domain.OmokInfo;
import com.piebin.piebot.omok.domain.OmokRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OmokInfoRepository extends JpaRepository<OmokInfo, Long> {
    void deleteAllByRoom(OmokRoom omokRoom);

    Optional<OmokInfo> findByRoomAndPosition(OmokRoom room, String position);

    List<OmokInfo> findAllByRoom(OmokRoom room);
}
