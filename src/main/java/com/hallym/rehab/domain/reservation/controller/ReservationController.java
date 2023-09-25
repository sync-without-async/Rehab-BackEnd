package com.hallym.rehab.domain.reservation.controller;

import com.hallym.rehab.domain.reservation.dto.ReservationRequestDTO;
import com.hallym.rehab.domain.reservation.dto.ReservationResponseByAdminDTO;
import com.hallym.rehab.domain.reservation.dto.ReservationResponseByUserDTO;
import com.hallym.rehab.domain.reservation.service.ReservationService;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/reservation-admin/{mid}")
    public PageResponseDTO<ReservationResponseByAdminDTO> getReservationListByAdmin(@PathVariable(name = "mid") String mid,
                                                                                    PageRequestDTO pageRequestDTO) {
        return reservationService.getListByAdmin(mid, pageRequestDTO);
    }

    @GetMapping("/reservation-user/{mid}")
    public PageResponseDTO<ReservationResponseByUserDTO> getReservationListByUser(@PathVariable(name = "mid") String mid,
                                                                                  PageRequestDTO pageRequestDTO) {
        return reservationService.getListByUser(mid, pageRequestDTO);
    }

    @PostMapping("/reservation")
    public ResponseEntity<String> createReservation(@RequestBody ReservationRequestDTO requestDTO) {
        String result = reservationService.createReservation(requestDTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/reservation/{rvno}")
    public ResponseEntity<String> cancleReservation(@PathVariable(name = "rvno") Long rvno) {
        String result = reservationService.cancleReservation(rvno);
        return ResponseEntity.ok(result);
    }
}