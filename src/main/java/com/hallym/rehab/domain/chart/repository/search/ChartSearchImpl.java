package com.hallym.rehab.domain.chart.repository.search;

import com.hallym.rehab.domain.chart.MetricsUtil;
import com.hallym.rehab.domain.chart.dto.ChartListAllDTO;
import com.hallym.rehab.domain.chart.dto.RecordDTO;
import com.hallym.rehab.domain.chart.entity.Chart;
import com.hallym.rehab.domain.chart.entity.QChart;
import com.hallym.rehab.domain.chart.entity.QRecord;
import com.hallym.rehab.domain.user.entity.MemberRole;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ChartSearchImpl extends QuerydslRepositorySupport implements ChartSearch {

    private final MetricsUtil metricsUtil;

    public ChartSearchImpl(MetricsUtil metricsUtil) {
        super(Chart.class);
        this.metricsUtil = metricsUtil;
    }

    @Override
    public Page<ChartListAllDTO> searchChartWithRecord(String mid, MemberRole role, String[] types, String keyword, String sortBy, Pageable pageable) {
        QChart chart = QChart.chart;
        QRecord record = QRecord.record;

        JPQLQuery<Chart> ChartIdQuery = from(chart)
                .join(chart.recordSet, record)
                .where(role == MemberRole.DOCTOR ? chart.doctor.mid.eq(mid) : chart.therapist.mid.eq(mid))
                .distinct();

        if ("oldest".equals(sortBy)) {
            ChartIdQuery.orderBy(chart.cno.asc());
        } else {
            ChartIdQuery.orderBy(chart.cno.desc());
        }

        // is_deleted가 false인 chart만 포함
        BooleanBuilder is_deleted = new BooleanBuilder();
        is_deleted.or(chart.is_deleted.eq(false));
        ChartIdQuery.where(is_deleted);

        if( (types != null) && keyword != null ){ //검색조건
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            String type = String.join("", types);

            switch (type) {
                case "patient":
                    booleanBuilder.or(chart.patientName.contains(keyword));
                    break;
                case "therapist":
                    booleanBuilder.or(chart.therapist.name.contains(keyword));
                    break;
            }

            ChartIdQuery.where(booleanBuilder);
        }


        this.getQuerydsl().applyPagination(pageable, ChartIdQuery);

        List<Long> chartIds = ChartIdQuery.select(chart.cno).fetch();
        long total = ChartIdQuery.fetchCount();

        // 첫 번째 쿼리에서 가져온 ID 리스트에 페이징 적용
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), chartIds.size());
        List<Long> pagedChartIds = chartIds.subList(start, end);

        log.info("pagedChartIds의 크기--------" + pagedChartIds);


        // 두 번째 쿼리: 첫 번째 쿼리에서 가져온 ID를 사용하여 Chart와 연관된 Record를 fetch
        JPQLQuery<Tuple> tupleJPQLQuery = from(chart)
                .join(chart.recordSet, record)
                .where(chart.cno.in(pagedChartIds))
                .groupBy(chart.cno)
                .select(chart, record.countDistinct());

        if ("oldest".equals(sortBy)) {
            // 오래된 차트부터 정렬 (cno 오름차순)
            tupleJPQLQuery.orderBy(chart.cno.asc());
        } else {
            // 최신 차트부터 정렬 (cno 내림차순)
            tupleJPQLQuery.orderBy(chart.cno.desc());
        }


        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        log.info("tupleList---------" + tupleList);


        List<ChartListAllDTO> dtoList = tupleList.stream()
                .map(tuple -> {
                    Chart chart1 = (Chart) tuple.get(chart);

                    log.info("chart1---------" + chart1);

                    List<RecordDTO> medicalRecords = chart1.getRecordSet().stream()
                            .map(RecordDTO::of)
                            .collect(Collectors.toList());

                    double metrics = metricsUtil.getRateMetricsByPatientId(chart1.getPatient().getMid());

                    log.info("metrics-----" + metrics);

                    ChartListAllDTO chartListAllDTO = ChartListAllDTO.builder()
                            .cno(chart1.getCno())
                            .cd(chart1.getCd())
                            .phone(chart1.getPhone())
                            .sex(chart1.getSex())
                            .birth(chart1.getBirth())
                            .patient_name(chart1.getPatientName())
                            .patient_id(chart1.getPatient() != null ? chart1.getPatient().getMid() : null)
                            .doctor_name(chart1.getDoctor().getName())
                            .therapist_name(chart1.getTherapist().getName())
                            .regDate(LocalDate.from(chart1.getRegDate()))
                            .medicalRecords(medicalRecords)
                            .metrics_rate(metrics)
                            .build();

                    return chartListAllDTO;
                })
                .collect(Collectors.toList());
        log.info("ChartList------->" + dtoList);

        return new PageImpl<>(dtoList, pageable, total); // 변환된 ChartListAllDTO 리스트로 PageImpl 생성
    }

}