package com.hallym.rehab.domain.program.service;

import com.hallym.rehab.domain.program.dto.act.ActResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramDetailResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramHistoryDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramListResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramRequestDTO;
import com.hallym.rehab.domain.program.entity.Program;

import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.repository.MemberRepository;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;

import java.util.Comparator;
import java.util.List;



public interface ProgramService {

    ProgramDetailResponseDTO getProgramOne(Long pno, String mid);

    ProgramHistoryDTO getProgramHistoryOne(Long pno, String mid);
    PageResponseDTO<ProgramListResponseDTO> getProgramList(PageRequestDTO pageRequestDTO);

    List<ProgramListResponseDTO> getProgramHistoryList(String mid);

    String modifyProgramOne(Long pno, ProgramRequestDTO programRequestDTO);

    String deleteProgramOne(Long pno, ProgramRequestDTO programRequestDTO);

    Long createProgram(ProgramRequestDTO programRequestDTO);

    String addProgramHistory(Long pno, String mid);

    String cancelProgram(Long pno, String mid);

}
