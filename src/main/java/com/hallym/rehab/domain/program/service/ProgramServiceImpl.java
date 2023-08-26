package com.hallym.rehab.domain.program.service;


import com.hallym.rehab.domain.program.dto.program.ProgramDetailResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramMainResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramRequestDTO;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.ProgramVideo;
import com.hallym.rehab.domain.program.repository.ProgramRepository;
import com.hallym.rehab.domain.user.repository.MemberRepository;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Set;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProgramServiceImpl implements ProgramService{

    private final MemberRepository memberRepository;
    private final ProgramRepository programRepository;

    @Override
    public ProgramDetailResponseDTO getProgramOne(Long pno) {

        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));

        Set<ProgramVideo> programVideo = program.getProgramVideo();

        ProgramDetailResponseDTO programRequestDTO = entitesToProgramDetailResponseDTO(program, programVideo);
        log.info("-------ProgramDTO: " + programRequestDTO);

        return programRequestDTO;
    }

    @Override
    public String modifyProgramOne(Long pno, ProgramRequestDTO programRequestDTO) {
        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));

        program.modifyProgram(programRequestDTO.getProgramTitle(), programRequestDTO.getDescription(),
                programRequestDTO.getCategory(), programRequestDTO.getPosition());
        programRepository.save(program);

        return "Program modify successfully.";
    }

    @Override
    public String deleteProgramOne(Long pno) {
        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));

        program.setIs_deleted(Boolean.TRUE);
        programRepository.save(program);

        return "Program delete successfully.";
    }

    @Override
    public PageResponseDTO<ProgramMainResponseDTO> getProgramList(PageRequestDTO pageRequestDTO) {

        return null;
    }

    @Override
    public String createProgram(ProgramRequestDTO programRequestDTO) {
        Program program = programRequestDtoToProgram(programRequestDTO);
        programRepository.save(program);
        return "Program create successfully.";
    }
}
