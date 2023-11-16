package com.hallym.rehab.domain.program.service;

import com.hallym.rehab.domain.program.dto.MetricsUpdateRequestDTO;
import com.hallym.rehab.domain.program.dto.ProgramRequestDTO;

public interface ProgramService {
    String createProgramAndDetail(ProgramRequestDTO programRequestDTO);

    String updateProgramAndDetail(ProgramRequestDTO programRequestDTO, String patient_id);

    String updateMetrics(MetricsUpdateRequestDTO metricsUpdateRequestDTO);
}
