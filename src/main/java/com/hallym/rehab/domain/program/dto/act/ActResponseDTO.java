package com.hallym.rehab.domain.program.dto.act;

import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActResponseDTO {
    private Long vno;
    private String actName;
    private double matrix;
}
