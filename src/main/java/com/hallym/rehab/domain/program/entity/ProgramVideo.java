package com.hallym.rehab.domain.program.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@ToString(exclude = {"program", "programVideo_members"})
@NoArgsConstructor
@Entity
public class ProgramVideo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vno;

    @Column(name = "actName")
    private String ActName; // 동작 이름

    @Column(name = "ord")
    private Long ord; // 한 프로그램에서 몇번째 동작인지

    @Column(name = "guideVideo")
    private String GuideVideoURL; // 동영상 URL로 접근할 URL

    @Column(name = "guideJson")
    private String JsonURL; // JSON 파일 접근할 URL

    @Column(name = "videoObjectName")
    private String GuideVideoObjectPath; // Video 파일 삭제하기 위한 ObjectPath

    @Column(name = "jsonObjectName")
    private String JsonObjectPath; // Json 파일 삭제하기 위한 ObjectPath

    @OneToMany(mappedBy = "programVideo")
    private List<ProgramVideo_Member> programVideo_members = new ArrayList<>();

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pno", nullable = false)
    private Program program;

    public void setProgram(Program program) {
        this.program = program;
    }

    public void setOrd(Long ord) {
        this.ord = ord;
    }

    public void changeProgramVideo(String GuideVideoURL, String JsonURL,
                                   String GuideVideoObjectPath, String JsonObjectPath, Long ord) {
        this.GuideVideoURL = GuideVideoURL;
        this.JsonURL = JsonURL;
        this.GuideVideoObjectPath = GuideVideoObjectPath;
        this.JsonObjectPath = JsonObjectPath;
        this.ord = ord;
    }

    public void changeProgram(Program program){ //Program 엔티티 삭제 시 ProgramVideo 객체의 참조도 변경하기 위한 메소드
        this.program = program;
    }
}
