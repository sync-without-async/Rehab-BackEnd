package com.hallym.rehab.domain.program.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetricsUpdateRequestDTO {
    private String patient_id;
    private Long pno;
    private Long vno;
    private int ord;
    private double metrics;
}
