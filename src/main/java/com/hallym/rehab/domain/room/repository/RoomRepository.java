package com.hallym.rehab.domain.room.repository;

import com.hallym.rehab.domain.room.entity.Room;
import com.hallym.rehab.domain.room.dto.RoomResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {

    @Query("SELECT r FROM Room r WHERE r.admin.mid = :admin_id AND r.user.mid = :user_id")
    Optional<Room> findByAdminAndUser(
            @Param("admin_id") String admin_id, @Param("user_id") String user_id);

    @Query("SELECT new com.hallym.rehab.domain.room.dto.RoomResponseDTO(r.rno, r.admin.mid, r.user.mid)" +
            " FROM Room r WHERE r.admin.mid = :admin_id")
    List<RoomResponseDTO> findByRoomListByAdmin(@Param("admin_id") String admin_id);

    @Query("SELECT new com.hallym.rehab.domain.room.dto.RoomResponseDTO(r.rno, r.admin.mid, r.user.mid)" +
            " FROM Room r WHERE r.user.mid = :user_id")
    List<RoomResponseDTO> findByRoomListByUser(@Param("user_id") String user_id);
}
