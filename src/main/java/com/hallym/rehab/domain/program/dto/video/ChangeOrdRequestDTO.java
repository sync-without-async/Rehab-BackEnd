package com.hallym.rehab.domain.program.dto.video;

import lombok.Data;

import java.util.Map;

@Data
public class ChangeOrdRequestDTO {
    private Map<Long, Long> ord_map;
}
