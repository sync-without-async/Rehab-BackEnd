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

    // Path to directly access objects stored in object storage
    private String videoURL;
    private String thumbnailURL;

    @QueryProjection
    public VideoResponseDTO(Long vno, String title, String description,
                            Tag tag, double playTime, String videoURL, String thumbnailURL) {
        this.vno = vno;
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.playTime = playTime;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }
}
