package com.hallym.rehab.domain.video.dto;

import com.hallym.rehab.domain.video.entity.Tag;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class VideoResponseDTO {
    private Long vno;
    private String title; // 동작 제목
    private String description; // 동작 설명
    private Tag tag; // 동작 태그
    private double playTime; // FRONT 에서 쓸 영상 시간
    private String videoURL; // 동영상 URL (실제로 Client, AI 서버에서 사용)

    @QueryProjection
    public VideoResponseDTO(Long vno, String title, String description, Tag tag, double playTime, String videoURL) {
        this.vno = vno;
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.playTime = playTime;
        this.videoURL = videoURL;
    }
}
