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
    private String admin_id;
    private String user_id;
    private String content;
    private LocalDate date;
    private int index;
}
