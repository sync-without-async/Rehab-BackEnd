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

    ProgramDTO getProgramOne(Long pno);

    String modifyProgramOne(Long pno, ProgramRequestDTO programRequestDTO, MultipartFile videoFile, MultipartFile jsonFile);

    String deleteProgramOne(Long pno);

    PageResponseDTO<ProgramDTO> getProgramList(PageRequestDTO pageRequestDTO);

    public void uploadFileToS3(MultipartFile videoFile, MultipartFile jsonFile, Program program);
    public String createProgram(ProgramRequestDTO programRequestDTO, MultipartFile  videoFile, MultipartFile jsonFile);
    public File convertMultipartFileToFile(MultipartFile multipartFile, String fileName);
    public void setAcl(AmazonS3 s3, String ObjectPath);
    public Program programRequestDtoToProgram(ProgramRequestDTO programRequestDTO);
    void deleteFileFromS3(String guideVideoObjectPath, String jsonObjectPath);

    default Program dtoToEntity(ProgramRequestDTO programRequestDTO){

        return Program.builder()
                .programTitle(programRequestDTO.getProgramTitle())
                .description(programRequestDTO.getDescription())
                .category(programRequestDTO.getCategory())
                .position(programRequestDTO.getPosition())
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
