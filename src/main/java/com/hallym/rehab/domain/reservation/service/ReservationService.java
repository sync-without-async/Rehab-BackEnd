package com.hallym.rehab.domain.reservation.service;

import com.hallym.rehab.domain.reservation.dto.ReservationRequestDTO;

public interface ReservationService {
    String createReservation(ReservationRequestDTO requestDTO);
}
