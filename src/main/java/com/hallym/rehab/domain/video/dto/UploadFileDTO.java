package com.hallym.rehab.domain.video.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileDTO {
    private String videoURL;
    private String jsonURL;
    private String thumbnailURL;
    private String videoPath;
    private String jsonPath;
    private String thumbnailPath;
}