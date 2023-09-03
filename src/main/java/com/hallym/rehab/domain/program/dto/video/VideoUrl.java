package com.hallym.rehab.domain.program.dto.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoUrl {
    private Long vno;
    private String url;
}
