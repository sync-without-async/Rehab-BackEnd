package com.hallym.rehab.domain.chart.repository;

import com.hallym.rehab.domain.chart.entity.Chart;
import com.hallym.rehab.domain.chart.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long>{

    @Query("SELECT r FROM Record r WHERE r.chart = :chart ORDER BY r.schedule ASC ")
    List<Record> findRecordByChart(@Param("chart") Chart chart);

}
