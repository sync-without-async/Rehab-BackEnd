package com.hallym.rehab.domain.program.service;

import com.hallym.rehab.domain.program.dto.program.ProgramDetailResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramMainResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramRequestDTO;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.ProgramVideo;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;

import java.util.Set;

public interface ProgramService {

    ProgramDetailResponseDTO getProgramOne(Long pno);
    PageResponseDTO<ProgramMainResponseDTO> getProgramList(PageRequestDTO pageRequestDTO);
    String modifyProgramOne(Long pno, ProgramRequestDTO programRequestDTO);
    String deleteProgramOne(Long pno);
    public String createProgram(ProgramRequestDTO programRequestDTO);
    default Program programRequestDtoToProgram(ProgramRequestDTO programRequestDTO){

        return Program.builder()
                .programTitle(programRequestDTO.getProgramTitle())
                .description(programRequestDTO.getDescription())
                .category(programRequestDTO.getCategory())
                .position(programRequestDTO.getPosition())
                .build();
    }

    default ProgramDetailResponseDTO entitesToProgramDetailResponseDTO(Program program, Set<ProgramVideo> programVideo){

//        programVideo.forEach();

        return ProgramDetailResponseDTO.builder()
                .pno(program.getPno())
                .programTitle(program.getProgramTitle())
                .description(program.getDescription())
                .category(program.getCategory())
                .position(program.getPosition())
                .regDate(program.getRegDate())
                .build();
    }

}
