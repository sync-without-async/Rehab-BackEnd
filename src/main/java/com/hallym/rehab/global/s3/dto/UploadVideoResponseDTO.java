package com.hallym.rehab.global.s3.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadVideoResponseDTO {
    private String videoURL;
    private String jsonURL;
    private String thumbnailURL;
    private String videoPath;
    private String jsonPath;
    private String thumbnailPath;
}