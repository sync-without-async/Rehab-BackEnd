package com.hallym.rehab.domain.reservation.repository;

import com.hallym.rehab.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE (r.patient.mid = :mid or r.staff.mid = :mid)")
    Page<Reservation> findByMid(@Param("mid") String mid, Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE r.patient.mid = :patient_id ORDER BY r.room.regDate ASC")
    List<Reservation> findAllByPatientMidWithSummary(@Param("patient_id") String patientId);
}
