package com.hallym.rehab.domain.program.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hallym.rehab.domain.video.entity.Video;
import com.hallym.rehab.domain.video.entity.VideoMetrics;
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
public class ProgramDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pdno;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pno", nullable = false)
    private Program program;

    @OneToOne
    @JoinColumn(name = "vno", referencedColumnName = "vno")
    private Video video;

    @OneToOne
    @JoinColumn(name = "vmno", referencedColumnName = "vmno")
    private VideoMetrics videoMetrics;

    private int ord; // 비디오의 순서

    public void setVideoMetrics(VideoMetrics videoMetrics) {
        this.videoMetrics = videoMetrics;
    }

    public void changeOrd(int ord, Video video) {
        this.ord = ord;
        this.video = video;
    }
}
