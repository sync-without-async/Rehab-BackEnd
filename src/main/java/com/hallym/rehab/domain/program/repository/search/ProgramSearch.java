package com.hallym.rehab.domain.program.repository.search;

import com.hallym.rehab.domain.program.dto.program.ProgramListResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProgramSearch {

    Page<ProgramListResponseDTO> searchProgramList(String[] types, String keyword, Pageable pageable);

}
