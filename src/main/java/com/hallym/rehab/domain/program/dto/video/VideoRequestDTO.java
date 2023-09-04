package com.hallym.rehab.domain.program.dto.video;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;

@Data
@Builder
public class VideoRequestDTO {
    private String actName;
    private double playTime;
    private Long frame;
    private Long guideWidth; // 가이드 영상의 Width
    private Long guideHeight; // 가이드 영상의 Height
    @Builder.Default
    private MultipartFile[] files = new MultipartFile[2];
}
