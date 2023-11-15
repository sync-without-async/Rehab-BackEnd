package com.hallym.rehab.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestDTO {
    private String staff_id;
    private String patient_id;
    private String content; // 예약 희망 사유
    private LocalDate date;
    private int index;
}
