package com.hallym.rehab.domain.program.service;

import com.hallym.rehab.domain.program.dto.ProgramDTO;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.repository.ProgramRepository;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProgramServiceImpl implements ProgramService{

    private final ProgramRepository programRepository;

    @Override
    public ProgramDTO getProgramOne(Long bno) {

        Program program = programRepository.findById(bno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + bno));

        ProgramDTO programDTO = entityToDTO(program);
        log.info("-------ProgramDTO: " + programDTO);

        return programDTO;
    }

    @Override
    public String modifyProgramOne(Long bno, ProgramDTO programDTO) {

        Program program = programRepository.findById(bno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + bno));

        program.modifyProgram(programDTO.getProgramTitle(), programDTO.getDescription(), programDTO.getCategory(), programDTO.getPosition());

        program.clearProgramVideo();

        programRepository.save(program);

        return "Program modify successfully.";
    }

    @Override
    public String deleteProgramOne(Long bno) {

        Program program = programRepository.findById(bno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + bno));

        program.setIs_deleted(Boolean.TRUE);
        programRepository.save(program);

        return "Program delete successfully.";
    }

    @Override
    public PageResponseDTO<ProgramDTO> getProgramList(PageRequestDTO pageRequestDTO) {

        return null;
    }
}
