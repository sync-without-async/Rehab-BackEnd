package com.hallym.rehab.domain.program.dto.act;

import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActResponseDTO {
    private Long ord; //등록 순서
    private String actName;
    private double metrics;
    private Long frame;
}
