package com.hallym.rehab.domain.program.service;

import com.amazonaws.services.s3.AmazonS3;
import com.hallym.rehab.domain.program.dto.ProgramRequestDTO;
import com.hallym.rehab.domain.program.entity.Program;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public interface ProgramService {
    public void uploadFileToS3(MultipartFile videoFile, MultipartFile jsonFile, Program program);
    public Program createProgram(ProgramRequestDTO programRequestDTO);
    public File convertMultipartFileToFile(MultipartFile multipartFile, String fileName);
    public void setAcl(AmazonS3 s3, String ucketName, String guideVideoObjectPath);
    public Program programRequestDtoToProgram(ProgramRequestDTO programRequestDTO);
}
