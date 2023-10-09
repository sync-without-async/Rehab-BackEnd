package com.hallym.rehab.domain.video.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.video.dto.VideoDetailResponseDTO;
import com.hallym.rehab.domain.video.dto.VideoResponseDTO;
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
public class Video extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vno;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid", nullable = false)
    private Admin admin; // 물리치료사 연관관계

    private String title; // 동작 제목

    private String description; // 동작 설명

    @Enumerated(value = EnumType.STRING)
    private Tag tag; // 동작 태그

    private Long frame; // AI 에서 쓸 영상 프레임

    private double playTime; // FRONT 에서 쓸 영상 시간

    private String videoURL; // 동영상 URL (실제로 Client, AI 서버에서 사용)

    private String jsonURL; // json URL (실제로 Client, AI 서버에서 사용)

    private String thumbnailURL; // thumbnail URL (Used by Cilent and AI Server)

    private String videoPath; // Object Storage 에서의 video 경로 (삭제시 사용)

    private String jsonPath; // Object Storage 에서의 json 경로 (삭제시 사용)

    private String thumbnailPath; // (Used when deleting)

    public VideoDetailResponseDTO toDetailDTO() {
        return VideoDetailResponseDTO.builder()
                .vno(vno)
                .title(title)
                .description(description)
                .videoURL(videoURL)
                .build();
    }
}
