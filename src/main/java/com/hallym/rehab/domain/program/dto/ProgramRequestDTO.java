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
public class ProgramRequestDTO {
    private String adminId; // 물리치료사
    private String userId;
    private String description;
    private Map<Integer, Long> ord_map; // ord, vno -> ord는 중복 X
}
