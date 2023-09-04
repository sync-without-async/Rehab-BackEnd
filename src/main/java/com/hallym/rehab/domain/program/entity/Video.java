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
@ToString(exclude = {"program", "video_members"})
@NoArgsConstructor
@Entity
public class Video extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vno;

    @Column(name = "actName")
    private String ActName; // 동작 이름

    @Column(name = "playTime")
    private double playTime;

    @Column(name = "frame")
    private Long frame; // 영상 몇 프레임인지

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

    @Column(name = "guideWidth")
    private Long guideWidth; // 가이드 영상의 Width

    @Column(name = "guideHeight")
    private Long guideHeight; // 가이드 영상의 Height

    @Builder.Default
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Video_Member> video_members = new ArrayList<>();

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

}
