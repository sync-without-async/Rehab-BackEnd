package com.hallym.rehab.domain.program.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramResponseDTO {
    private String title; // 과제 이름 -> Video의 title
    private int ord; // 영상 순서 -> ProgramDetail의 ord
    private Long vno; // 영상 PK
    private double metrics; // 영상 정확도
}
