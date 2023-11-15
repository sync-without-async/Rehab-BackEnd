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

    @Query("SELECT r FROM Room r WHERE r.staff.mid = :staff_id AND r.patient.mid = :patient_id")
    Optional<Room> findByStaffAndPatient(
            @Param("staff_id") String staff_id, @Param("patient_id") String patient_id);

    @Query("SELECT new com.hallym.rehab.domain.room.dto.RoomResponseDTO(r.rno, r.staff.mid, r.patient.mid)" +
            " FROM Room r WHERE r.staff.mid = :staff_id")
    List<RoomResponseDTO> findByRoomListByStaff(@Param("staff_id") String staff_id);

    @Query("SELECT new com.hallym.rehab.domain.room.dto.RoomResponseDTO(r.rno, r.staff.mid, r.patient.mid)" +
            " FROM Room r WHERE r.patient.mid = :patient_id")
    List<RoomResponseDTO> findByRoomListByPatient(@Param("patient_id") String patient_id);
}
