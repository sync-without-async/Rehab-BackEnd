package com.hallym.rehab.domain.program.dto.program;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hallym.rehab.domain.program.entity.Category;
import com.hallym.rehab.domain.program.entity.Position;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramMainResponseDTO {
    /**
     * Program 의 메인페이지에 반환할 DTO
     * List 로 반환
     */

    private Long pno;

    private String programTitle;

    private String description;

    private Category category;

    private Position position;

//    private List<String> programVideoFile;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;

}
