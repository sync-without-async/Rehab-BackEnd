package com.hallym.rehab.domain.chart.service;

import com.hallym.rehab.domain.chart.dto.ChartRequestDTO;
import com.hallym.rehab.domain.chart.dto.ChartResponseDTO;

public interface ChartService {

    ChartResponseDTO getChartDetails(Long cno);

    void registerChartDetails(ChartRequestDTO registerDTO);

    String modifyChartDetails(ChartRequestDTO modifyDTO);

    String deleteChartDetails(Long cno);

}
