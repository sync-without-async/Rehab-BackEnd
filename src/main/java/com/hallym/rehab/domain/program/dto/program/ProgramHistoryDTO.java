package com.hallym.rehab.domain.program.dto.program;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hallym.rehab.domain.program.dto.act.ActResponseDTO;
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
public class ProgramHistoryDTO {

    private String mid;
    private String programName;
    private Category category;
    private Position position;
    private List<ActResponseDTO> actResponseDTO;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;
}
