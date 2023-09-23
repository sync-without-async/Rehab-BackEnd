package com.hallym.rehab.domain.reservation.repository;

import com.hallym.rehab.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.user.mid = :mid or r.admin.mid = :mid " +
            "ORDER BY r.date ASC, r.index ASC")
    List<Reservation> findByMid(@Param("mid") String mid);
}
