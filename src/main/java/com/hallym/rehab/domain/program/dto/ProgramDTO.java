package com.hallym.rehab.domain.program.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hallym.rehab.domain.program.entity.Category;
import com.hallym.rehab.domain.program.entity.Position;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramDTO {

    private Long pno;

    private String programTitle;

    private String description;

    private Category category;

    private Position position;

    private List<String> programVideoFile;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;

}
