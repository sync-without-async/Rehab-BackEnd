package com.hallym.rehab.domain.room.repository;

import com.hallym.rehab.domain.room.domain.Audio;
import com.hallym.rehab.domain.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AudioRepository extends JpaRepository<Audio, Long> {
    Optional<Audio> findByRoom(Room room);
}
