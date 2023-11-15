package com.hallym.rehab.domain.chart.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Chart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cno;

    @Column(nullable = false)
    private String cd; //질병분류기호

    @Column(nullable = false)
    private String patientName; //환자성함

    private String phone;

    private String sex;

    private LocalDate birth; //생년월일

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_mid", nullable = false)
    private Staff doctor; // 담당의

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "therapist_mid", nullable = false)
    private Staff therapist; // 담당재활치료사

    private String medicalRecord; //진료 기록

    private String exerciseRequest; //운동요청서

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_mid", nullable = false)
    private Patient patient;


}
