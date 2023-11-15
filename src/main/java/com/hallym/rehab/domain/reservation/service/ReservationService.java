package com.hallym.rehab.domain.reservation.service;

import com.hallym.rehab.domain.reservation.dto.ReservationRequestDTO;
import com.hallym.rehab.domain.reservation.dto.ReservationResponseByStaffDTO;
import com.hallym.rehab.domain.reservation.dto.ReservationResponseByPatientDTO;
import com.hallym.rehab.domain.reservation.entity.Time;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    PageResponseDTO<ReservationResponseByStaffDTO> getListByStaff(String mid, PageRequestDTO pageRequestDTO);
    PageResponseDTO<ReservationResponseByPatientDTO> getListByUser(String mid, PageRequestDTO pageRequestDTO);
    List<Time> getReservedTime(String staffId, LocalDate localDate);
    String createReservation(ReservationRequestDTO requestDTO);
    String cancleReservation(Long rvno);
}
