package com.hallym.rehab.domain.program.entity;


import com.hallym.rehab.domain.user.entity.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Video_Member {
    /**
     * 유저마다 한 운동영상에 대한 Matrix 정보를 표시하기 위해 생성한 테이블
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vmno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vno")
    private Video video;

    @Builder.Default
    private double metrics = 0;

    public void changeMetrics(double metrics) {
        this.metrics = metrics;
    }
}
