package com.hallym.rehab.domain.program.service;

import com.amazonaws.services.s3.AmazonS3;
import com.hallym.rehab.domain.program.dto.ProgramDTO;
import com.hallym.rehab.domain.program.dto.ProgramRequestDTO;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface ProgramService {

    ProgramDTO getProgramOne(Long bno);

    String modifyProgramOne(Long bno, ProgramDTO programDTO);

    String deleteProgramOne(Long bno);

    PageResponseDTO<ProgramDTO> getProgramList(PageRequestDTO pageRequestDTO);

    public void uploadFileToS3(MultipartFile videoFile, MultipartFile jsonFile, Program program);
    public Program createProgram(ProgramRequestDTO programRequestDTO);
    public File convertMultipartFileToFile(MultipartFile multipartFile, String fileName);
    public void setAcl(AmazonS3 s3, String ucketName, String guideVideoObjectPath);
    public Program programRequestDtoToProgram(ProgramRequestDTO programRequestDTO);

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
