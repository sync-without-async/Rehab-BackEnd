package com.hallym.rehab.domain.program.service;

import com.hallym.rehab.domain.program.dto.ProgramDTO;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;

public interface ProgramService {

    ProgramDTO getProgramOne(Long bno);

    String modifyProgramOne(Long bno, ProgramDTO programDTO);

    String deleteProgramOne(Long bno);

    PageResponseDTO<ProgramDTO> getProgramList(PageRequestDTO pageRequestDTO);

    default Program dtoToEntity(ProgramDTO programDTO){

        return Program.builder()
                .pno(programDTO.getPno())
                .programTitle(programDTO.getProgramTitle())
                .description(programDTO.getDescription())
                .category(programDTO.getCategory())
                .position(programDTO.getPosition())
                .build();
    }

    default ProgramDTO entityToDTO(Program program){

        return ProgramDTO.builder()
                .pno(program.getPno())
                .programTitle(program.getProgramTitle())
                .description(program.getDescription())
                .category(program.getCategory())
                .position(program.getPosition())
                .regDate(program.getRegDate())
                .build();
    }

}
