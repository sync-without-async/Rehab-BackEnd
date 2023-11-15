package com.hallym.rehab.domain.chart.entity;

import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = "chart")
public class Record extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long record_no;

    @Column(nullable = false)
    private LocalDate schedule; //외래 일정

    @Column(columnDefinition = "TEXT", nullable = false) //최대 64KB
    private String treatmentRecord; //진료 기록

    @Column(columnDefinition = "TEXT", nullable = false)
    private String exerciseRequest; //운동요청서

    @ManyToOne(fetch = FetchType.LAZY)
    private Chart chart;

}
