package com.hallym.rehab.domain.program.dto.video;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class VideoRequestDTO {
    private String actName;
    private double playTime;
    private Long frame;
    @Builder.Default
    private MultipartFile[] files = new MultipartFile[2];
}
