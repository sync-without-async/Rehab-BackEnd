package com.hallym.rehab.domain.program.dto.video;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VideoRequestDTO {
    private String actName;
    private double playTime;
    private Long frame;
    private MultipartFile[] files = new MultipartFile[2];
}
