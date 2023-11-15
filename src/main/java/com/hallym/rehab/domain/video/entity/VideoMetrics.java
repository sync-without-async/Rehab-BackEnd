package com.hallym.rehab.domain.video.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hallym.rehab.domain.program.entity.ProgramDetail;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VideoMetrics extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vmno;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne
    @JoinColumn(name = "pdno", referencedColumnName = "pdno")
    private ProgramDetail programDetail;

    private double metrics;

    public void updateMetrics(double metrics) {
        this.metrics = metrics;
    }
}
