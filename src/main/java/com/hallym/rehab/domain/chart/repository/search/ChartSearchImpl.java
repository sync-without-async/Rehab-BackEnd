package com.hallym.rehab.domain.chart.repository.search;

import com.hallym.rehab.domain.chart.entity.Chart;
import com.hallym.rehab.domain.chart.entity.QChart;
import com.hallym.rehab.domain.chart.entity.QRecord;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Slf4j
public class ChartSearchImpl extends QuerydslRepositorySupport implements ChartSearch {

    public ChartSearchImpl() {
        super(Chart.class);
    }

    @Override
    public Page<Chart> search(PageRequestDTO pageRequestDTO) {

        QChart chart = QChart.chart;

        JPQLQuery<Chart> query = from(chart);

//        query.where(chart.patientName.contains("1"));

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("cno").descending());

        this.getQuerydsl().applyPagination(pageable, query);

        List<Chart> list = query.fetch(); //목록 데이터 가져오기

        long total = query.fetchCount(); //항상 Long type 반환

        return new PageImpl<>(list, pageable, total);
    }

    @Override
    public Page<Chart> searchChartWithRecord(String doctor_id, PageRequestDTO pageRequestDTO) {


        QChart chart = QChart.chart;
        QRecord record = QRecord.record;

        JPQLQuery<Chart> query = from(chart)
                .join(chart.recordSet, record)  // chart.records는 Chart 엔티티의 Record 엔티티와의 관계를 나타냄
                .where(chart.doctor.mid.eq(doctor_id)) // doctor_id와 일치하는 Chart만 조회
                .fetchJoin();  // fetchJoin()을 사용하여 Chart 엔티티를 가져올 때 Record 엔티티도 같이 가져온다.

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("cno").descending());

        this.getQuerydsl().applyPagination(pageable, query);

        List<Chart> list = query.fetch(); //목록 데이터 가져오기

        long total = query.fetchCount(); //항상 Long type 반환

        return new PageImpl<>(list, pageable, total);
    }
}
