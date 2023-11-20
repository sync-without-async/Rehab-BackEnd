package com.hallym.rehab.domain.chart.service;

import com.hallym.rehab.domain.chart.dto.AIRecordDTO;
import com.hallym.rehab.domain.chart.dto.ChartListAllDTO;
import com.hallym.rehab.domain.chart.dto.ChartRequestDTO;
import com.hallym.rehab.domain.chart.dto.ChartResponseDTO;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;

import java.util.List;

public interface ChartService {

    ChartResponseDTO getChartDetailByPatient(String mid);

    ChartResponseDTO getChartDetailByStaff(Long cno);

    List<AIRecordDTO> getAIRecords(String patient_id);

    String registerChartDetails(ChartRequestDTO registerDTO);

    void deleteChartDetails(Long cno);

    PageResponseDTO<ChartListAllDTO> getChartList(String mid, PageRequestDTO pageRequestDTO);

}
