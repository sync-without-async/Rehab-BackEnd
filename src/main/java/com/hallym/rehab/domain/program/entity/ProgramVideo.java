package com.hallym.rehab.domain.program.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity
public class ProgramVideo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vno;

    @Column(name = "guideVideo")
    private String GuideVideoURL; // 동영상 URL로 접근할 URL

    @Column(name = "guideJson")
    private String JsonURL; // JSON 파일 접근할 URL

    @Column(name = "videoObjectName")
    private String GuideVideoObjectPath; // Video 파일 삭제하기 위한 ObjectPath

    @Column(name = "jsonObjectName")
    private String JsonObjectPath; // Json 파일 삭제하기 위한 ObjectPath

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pno", nullable = false)
    private Program program;

    public void changeProgram(Program program ){ //Program 엔티티 삭제 시 ProgramVideo 객체의 참조도 변경하기 위한 메소드
        this.program = program;
    }
}
