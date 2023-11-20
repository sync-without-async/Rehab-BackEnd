package com.hallym.rehab.domain.chart.repository.search;

import com.hallym.rehab.domain.chart.dto.ChartListAllDTO;
import com.hallym.rehab.domain.user.entity.MemberRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChartSearch {
    Page<ChartListAllDTO> searchChartWithRecord(String mid, MemberRole role, String[] types, String keyword, String sortBy, Pageable pageable);
}
