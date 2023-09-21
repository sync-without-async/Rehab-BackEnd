package com.hallym.rehab.domain.room.repository;

import com.hallym.rehab.domain.room.entity.Audio;
import com.hallym.rehab.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AudioRepository extends JpaRepository<Audio, Long> {
    Optional<Audio> findByRoom(Room room);
}
