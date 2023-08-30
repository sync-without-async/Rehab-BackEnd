package com.hallym.rehab.domain.program.service;

import com.hallym.rehab.domain.program.dto.act.ActResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramDetailResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramMainResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramRequestDTO;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.Video;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface ProgramService {

    ProgramDetailResponseDTO getProgramOne(Long pno, String mid);

    ProgramHistoryDTO getProgramHistoryOne(Long pno, String mid);
    PageResponseDTO<ProgramListResponseDTO> getProgramList(PageRequestDTO pageRequestDTO);

    List<ProgramListResponseDTO> getProgramHistoryList(String mid);

    String modifyProgramOne(Long pno, ProgramRequestDTO programRequestDTO);
    String deleteProgramOne(Long pno);
    Long createProgram(ProgramRequestDTO programRequestDTO);
    String registerProgram(Long pno, String mid);
    String cancelProgram(Long pno, String mid);
    default Program programRequestDtoToProgram(ProgramRequestDTO programRequestDTO){

        return Program.builder()
                .programTitle(programRequestDTO.getProgramTitle())
                .description(programRequestDTO.getDescription())
                .category(programRequestDTO.getCategory())
                .position(programRequestDTO.getPosition())
                .build();
    }
    default ProgramDetailResponseDTO entitesToProgramDetailResponseDTO(Program program, List<ActResponseDTO> actResponseDTOList){

        return ProgramDetailResponseDTO.builder()
                .pno(program.getPno())
                .programTitle(program.getProgramTitle())
                .description(program.getDescription())
                .category(program.getCategory())
                .position(program.getPosition())
                .actResponseDTO(actResponseDTOList)
                .regDate(program.getRegDate())
                .build();
    }
}
