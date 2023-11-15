package com.hallym.rehab.domain.chart.repository;

import com.hallym.rehab.domain.chart.entity.Chart;
import com.hallym.rehab.domain.chart.repository.search.ChartSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChartRepository extends JpaRepository<Chart, Long>, ChartSearch {


}
