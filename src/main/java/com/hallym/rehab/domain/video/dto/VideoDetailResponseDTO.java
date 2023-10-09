package com.hallym.rehab.domain.video.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class VideoDetailResponseDTO {
    private Long vno;
    private String title;
    private String description;

    // Path to directly access objects stored in object storage
    private String videoURL;
}
