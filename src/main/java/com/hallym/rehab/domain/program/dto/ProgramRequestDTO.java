package com.hallym.rehab.domain.program.dto;

import com.hallym.rehab.domain.program.entity.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProgramRequestDTO {
    private String mid; // 어떤 Admin이 등록했는지
    private String programTitle;
    private String description;
    private Category category;
}
