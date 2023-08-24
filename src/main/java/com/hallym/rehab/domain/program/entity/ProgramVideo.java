package com.hallym.rehab.domain.program.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity
public class ProgramVideo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vno;

    @Column(name = "guideVideo")
    private String GuideVideoPath;

    @Column(name = "guideJson")
    private String JsonPath;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pno", nullable = false)
    private Program program;

    public void changeProgram(Program program ){ //Program 엔티티 삭제 시 ProgramVideo 객체의 참조도 변경하기 위한 메소드
        this.program = program;
    }
}
