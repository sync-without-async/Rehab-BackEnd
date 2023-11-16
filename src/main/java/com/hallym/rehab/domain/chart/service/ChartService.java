package com.hallym.rehab.domain.chart.service;

import com.hallym.rehab.domain.chart.dto.ChartRequestDTO;
import com.hallym.rehab.domain.chart.dto.ChartResponseDTO;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;

public interface ChartService {

    ChartResponseDTO getChartDetails(String mid);

    String registerChartDetails(ChartRequestDTO registerDTO);

    void deleteChartDetails(Long cno);

    PageResponseDTO<ChartResponseDTO> getChartList(String doctor_id, PageRequestDTO pageRequestDTO);

}
