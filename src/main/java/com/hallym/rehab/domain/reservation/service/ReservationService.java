package com.hallym.rehab.domain.reservation.service;

import com.hallym.rehab.domain.reservation.dto.ReservationRequestDTO;
import com.hallym.rehab.domain.reservation.dto.ReservationResponseByAdminDTO;
import com.hallym.rehab.domain.reservation.dto.ReservationResponseByUserDTO;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;

public interface ReservationService {
    PageResponseDTO<ReservationResponseByAdminDTO> getListByAdmin(String mid, PageRequestDTO pageRequestDTO);
    PageResponseDTO<ReservationResponseByUserDTO> getListByUser(String mid, PageRequestDTO pageRequestDTO);
    String createReservation(ReservationRequestDTO requestDTO);
    String cancleReservation(Long rvno);
}
