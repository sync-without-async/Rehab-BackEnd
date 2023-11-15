package com.hallym.rehab.domain.chart.repository.search;

import com.hallym.rehab.domain.chart.entity.Chart;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface ChartSearch {

    Page<Chart> search(PageRequestDTO pageRequestDTO);

    Page<Chart> searchChartWithRecord(String doctor_id, PageRequestDTO pageRequestDTO);
}
