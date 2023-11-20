package com.hallym.rehab.domain.chart.controller;

import com.hallym.rehab.domain.chart.dto.AIRecordDTO;
import com.hallym.rehab.domain.chart.dto.ChartListAllDTO;
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
@RequiredArgsConstructor
@Slf4j
public class ChartController {

    private final ChartService chartService;

    /**
     * 환자는 본인의 차트 정보를 조회힌다.
     * @param patient_mid
     * @return ChartResponseDTO
     */
    @GetMapping("/chart/patient/{patient_mid}")
    public ChartResponseDTO getChartOneByPatient(@PathVariable String patient_mid) {

        return chartService.getChartDetailByPatient(patient_mid);
    }

    /**
     * 로그인한 의료진은 나의 환자 차트 목록에서 cno로 특정 차트를 상세 조회한다.
     * @param cno
     * @return ChartResponseDTO
     */
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_THERAPIST')")
    @GetMapping("/auth/chart/{cno}")
    public ChartResponseDTO getChartOneByStaff(@PathVariable Long cno) {

        return chartService.getChartDetailByStaff(cno);
    }

    /**
     * AI비대면 요약 정보 목록을 조회한다.
     * @param patient_mid
     * @return AIRecordDT의 List
     */
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_THERAPIST') or hasAuthority('ROLE_PATIENT')")
    @GetMapping("/auth/aiRecordList/{patient_mid}")
    public List<AIRecordDTO> getAIRecordList(@PathVariable String patient_mid) {

        return chartService.getAIRecords(patient_mid);
    }


    /**
     * 로그인 한 의사는 환자 차트를 신규 등록한다.
     * @param chartRequestDTO
     * @return 신규 환자 mid
     */
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/auth/chart/register")
    public String registerChart(@Valid @RequestBody ChartRequestDTO chartRequestDTO) {

        return chartService.registerChartDetails(chartRequestDTO);
    }

    /**
     * 의사는 차트를 삭제한다.
     * @param cno
     * @return 성공 시, "차트 삭제 완료" 반환
     */
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/auth/chart/delete")
    public ResponseEntity<String> deleteChart(@PathVariable Long cno) {

        chartService.deleteChartDetails(cno);
        return ResponseEntity.ok("차트 삭제 완료");
    }

    /**
     * 로그인한 의료진은 mid를 전달하여 내가 담당한 환자의 차트 목록을 조회한다.
     * @param mid #doctor pk or therapist pk
     * @return PageResponseDTO<ChartListAllDTO>
     */
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_THERAPIST')")
    @GetMapping("/auth/chart/list/{mid}")
    public PageResponseDTO<ChartListAllDTO> getChartList(PageRequestDTO pageRequestDTO, @PathVariable String mid){

        return chartService.getChartList(mid, pageRequestDTO);
    }


}
