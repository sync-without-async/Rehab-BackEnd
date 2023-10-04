package com.hallym.rehab.domain.program.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.program.dto.ProgramRequestDTO;
import com.hallym.rehab.domain.user.entity.Member;
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
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin; // 물리치료사 연관관계

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member user;

    private String description; // 과제 설명

    @Builder.Default
    @OneToMany(mappedBy = "pdno", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ProgramDetail> programDetailSet = new HashSet<>();

    public void changeDescription(String description) {
        this.description = description;
    }
}
