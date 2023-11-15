package com.hallym.rehab.domain.program.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Program extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff; // 물리치료사 연관관계

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    private String description; // 과제 설명

    @Builder.Default
    @OneToMany(mappedBy = "pdno", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ProgramDetail> programDetailSet = new HashSet<>();

    public void changeDescription(String description) {
        this.description = description;
    }
}
