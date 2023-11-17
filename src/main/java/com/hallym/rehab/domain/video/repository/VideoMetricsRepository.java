package com.hallym.rehab.domain.video.repository;

import com.hallym.rehab.domain.program.entity.ProgramDetail;
import com.hallym.rehab.domain.video.entity.VideoMetrics;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VideoMetricsRepository extends JpaRepository<VideoMetrics, Long> {

    @Query("SELECT vm FROM VideoMetrics vm WHERE vm.programDetail = :ProgramDetail")
    Optional<VideoMetrics> findByProgramDetail(@Param("ProgramDetail") ProgramDetail programDetail);

    @Query("SELECT vm FROM VideoMetrics vm WHERE vm.patient.mid = :patient_id")
    List<VideoMetrics> findMetricsByPatientId(@Param("patient_id") String patient_id);
}
