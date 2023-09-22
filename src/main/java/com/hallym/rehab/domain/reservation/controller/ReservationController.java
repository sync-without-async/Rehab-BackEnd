package com.hallym.rehab.domain.reservation.controller;

import com.hallym.rehab.domain.reservation.dto.ReservationRequestDTO;
import com.hallym.rehab.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reservation")
    public ResponseEntity<String> createReservation(@RequestBody ReservationRequestDTO requestDTO) {
        String result = reservationService.createReservation(requestDTO);
        return ResponseEntity.ok(result);
    }
}
