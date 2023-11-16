package com.hallym.rehab.domain.chart.controller;

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

@RestController
@RequestMapping("/chart")
@RequiredArgsConstructor
@Slf4j
public class ChartController {

    private final ChartService chartService;

    @GetMapping("/{mid}")
    public ChartResponseDTO getChartOne(@PathVariable String mid) {

        return chartService.getChartDetails(mid);
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
