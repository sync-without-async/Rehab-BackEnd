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
public class ReservationResponseByAdminDTO {
    private String userName;
    private String userId; // 환자 상세정보 보기 위한 Id
    private UUID rno;
    private LocalDate date;
    private int index;
}
