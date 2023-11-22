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
public class ReservationResponseByStaffDTO {
    private String patientName;
    private String patientId; // 환자 상세정보 보기 위한 Id
    private UUID rno;
    private Long rvno;
    private LocalDate date;
    private int index;
    private String content; // 진료 희망 사유
    private String summary; // 오디오 요약
    private boolean is_deleted;
}
