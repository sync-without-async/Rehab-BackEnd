package com.hallym.rehab.domain.program.service;

import com.amazonaws.services.s3.AmazonS3;
import com.hallym.rehab.domain.program.dto.upload.UploadFileDTO;
import com.hallym.rehab.domain.program.dto.video.SwapOrdRequestDTO;
import com.hallym.rehab.domain.program.dto.video.VideoRequestDTO;
import com.hallym.rehab.domain.program.entity.Program;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface VideoService {

    String createVideo(Long pno, Long ord, VideoRequestDTO videoRequestDTO);
    String deleteVideo(Long pno, Long ord);
    String swapVideoOrd(Long pno, SwapOrdRequestDTO swapOrdRequestDTO);
    UploadFileDTO uploadFileToS3(MultipartFile videoFile, MultipartFile jsonFile, Program program);
    File convertMultipartFileToFile(MultipartFile multipartFile, String fileName);
    void setAcl(AmazonS3 s3, String ObjectPath);
    void deleteFileFromS3(String guideVideoObjectPath, String jsonObjectPath);
}
