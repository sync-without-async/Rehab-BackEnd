package com.hallym.rehab.domain.program.dto.video;

import com.hallym.rehab.domain.program.entity.Category;
import com.hallym.rehab.domain.program.entity.Position;
import lombok.*;

import java.util.List;

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

    private List<String> programVideoFile;
}
