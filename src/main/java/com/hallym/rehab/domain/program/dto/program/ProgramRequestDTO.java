package com.hallym.rehab.domain.program.dto.program;

import com.hallym.rehab.domain.program.entity.Category;
import com.hallym.rehab.domain.program.entity.Position;
import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramRequestDTO {
    private String mid;
    private String programTitle;
    private String description;
    private Category category;
    private Position position;
}
