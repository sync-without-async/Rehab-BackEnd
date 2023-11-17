package com.hallym.rehab.domain.chart.controller;

import com.hallym.rehab.domain.chart.dto.AIRecordDTO;
import com.hallym.rehab.domain.chart.dto.ChartRequestDTO;
import com.hallym.rehab.domain.chart.dto.ChartResponseDTO;
import com.hallym.rehab.domain.chart.service.ChartService;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/chart")
@RequiredArgsConstructor
@Slf4j
public class ChartController {

    private final ChartService chartService;

    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    @GetMapping("/auth/patient/{patient_mid}")
    public ChartResponseDTO getChartOneByPatient(@PathVariable String patient_mid) {

        return chartService.getChartDetailByPatient(patient_mid);
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_THERAPIST')")
    @GetMapping("/auth/staff/{cno}")
    public ChartResponseDTO getChartOneByStaff(@PathVariable Long cno) {

        return chartService.getChartDetailByStaff(cno);
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_THERAPIST') or hasAuthority('ROLE_PATIENT')")
    @GetMapping("/auth/aiRecord/{patient_mid}")
    public List<AIRecordDTO> getAIRecordList(@PathVariable String patient_mid) {

        return chartService.getAIRecords(patient_mid);
    }


    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/auth/register")
    public String registerChart(@Valid @RequestBody ChartRequestDTO chartRequestDTO) {

        return chartService.registerChartDetails(chartRequestDTO);
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/auth/delete")
    public ResponseEntity<String> deleteChart(@PathVariable Long cno) {

        chartService.deleteChartDetails(cno);
        return ResponseEntity.ok("차트 삭제 완료");
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR') and authentication.principal.username == #doctor_id")
    @GetMapping("/auth/list/{doctor_id}")
    public PageResponseDTO<ChartResponseDTO> getChartList(PageRequestDTO pageRequestDTO, @PathVariable String doctor_id){

        return chartService.getChartList(doctor_id, pageRequestDTO);
    }


}
