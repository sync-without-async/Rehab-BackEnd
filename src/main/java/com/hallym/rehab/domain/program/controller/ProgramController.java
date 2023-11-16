package com.hallym.rehab.domain.program.controller;

import com.hallym.rehab.domain.program.dto.MetricsUpdateRequestDTO;
import com.hallym.rehab.domain.program.dto.ProgramRequestDTO;
import com.hallym.rehab.domain.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;

    @PostMapping("/program")
    public ResponseEntity<String> createProgramAndDetail(@RequestBody ProgramRequestDTO requestDTO) {
        String result = programService.createProgramAndDetail(requestDTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/program/{patient_id}")
    public ResponseEntity<String> updateProgramAndDetail(@RequestBody ProgramRequestDTO requestDTO,
                                                         @PathVariable("patient_id") String patient_id) {
        String result = programService.updateProgramAndDetail(requestDTO, patient_id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/metrics")
    public ResponseEntity<String> updateMetrics(@RequestBody MetricsUpdateRequestDTO requestDTO) {
        String result = programService.updateMetrics(requestDTO);
        return ResponseEntity.ok(result);
    }
}
