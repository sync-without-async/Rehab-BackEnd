package com.hallym.rehab.domain.reservation.repository;

import com.hallym.rehab.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
