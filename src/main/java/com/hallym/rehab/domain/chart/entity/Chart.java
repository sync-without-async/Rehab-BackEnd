package com.hallym.rehab.domain.chart.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Chart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cno; //chart의 고유번호

    @Column(nullable = false)
    private String cd; //질병분류기호

    @Column(nullable = false)
    private String patientName; //환자성함

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String sex;

    @Column(nullable = false)
    private LocalDate birth; //생년월일

    private Double metrics_rate;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_mid", nullable = false)
    private Staff doctor; // 담당의

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "therapist_mid", nullable = false)
    private Staff therapist; // 담당재활치료사

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "record_no", cascade = CascadeType.ALL)
    private Set<Record> recordSet = new HashSet<>();;

    @ColumnDefault("false")
    private boolean is_deleted;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_mid", nullable = false)
    private Patient patient;

    public void addPatient(Patient patient) {
        this.patient = patient;
    }

    public void addRecord(Record record){
        recordSet.add(record);
    }

}
