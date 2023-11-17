package com.hallym.rehab.domain.chart.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AIRecordDTO {

    private String staff_id;
    private String summary;
    private LocalDate regDate;
}
