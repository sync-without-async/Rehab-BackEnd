package com.hallym.rehab.domain.program.dto;

import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramVideoDTO {

    private Long vno;

    private String GuideVideoPath;

    private String JsonPath;
}
