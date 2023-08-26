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
public class ProgramDetailResponseDTO {
    /**
     * Program 의 상세 조회 페이지에 반환할 DTO
     */
    private Long pno;

    private String programTitle;

    private String description;

    private Category category;

    private Position position;

    private List<ActResponseDTO> actResponseDTO;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate; // 프로그램 등록시간
}
