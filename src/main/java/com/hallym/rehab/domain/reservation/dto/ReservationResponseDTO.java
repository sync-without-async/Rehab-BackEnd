package com.hallym.rehab.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDTO {
    private String admin_id;
    private String user_id;
    private UUID rno;
    private LocalDate date;
    private int index;
    // + role
    // + company
    // + 프로필 사진
}
