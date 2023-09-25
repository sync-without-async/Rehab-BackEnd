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
public class ReservationResponseByUserDTO {
    private String adminName;
    private UUID rno; // 화상채팅 룸 입장을 위한 room number
    private Long rvno; // 예약 취소를 위한 reservation number
    private LocalDate date;
    private int index;
    // + role
    // + company
    // + 프로필 사진
}
