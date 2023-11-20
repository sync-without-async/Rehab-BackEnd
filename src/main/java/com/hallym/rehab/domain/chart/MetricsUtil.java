package com.hallym.rehab.domain.chart;

import com.hallym.rehab.domain.video.entity.VideoMetrics;
import com.hallym.rehab.domain.video.repository.VideoMetricsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@RequiredArgsConstructor
public class MetricsUtil {

    private final VideoMetricsRepository videoMetricsRepository;

    /**
     * patient_id를 받아 Metrics들을 찾고 특정 한도를 넘는 것의 비율을 반환
     *
     * @param patient_id
     * @return rate of over70
     */
    public double getRateMetricsByPatientId(String patient_id) {
        List<VideoMetrics> metricsList = videoMetricsRepository.findMetricsByPatientId(patient_id);

        int allCount = metricsList.size();

        if (allCount == 0) {
            return 0;
        }

        int over60Count = (int) metricsList.stream()
                .filter(metrics -> metrics.getMetrics() >= 60)
                .count();

        return (double) over60Count / allCount * 100;
    }

}
