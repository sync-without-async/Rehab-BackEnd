package com.hallym.rehab.domain.program.dto.video;

import java.util.HashMap;
import com.hallym.rehab.domain.program.entity.Category;
import com.hallym.rehab.domain.program.entity.Position;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.Video;
import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoResponseDTO {
    private Long pno;
    private String programTitle;
    private String description;
    private Category category;
    private Position position;
    private List<VideoUrl> vno_videoUrl;
}
