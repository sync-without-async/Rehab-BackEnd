package com.hallym.rehab.domain.admin.service;

import com.amazonaws.services.s3.AmazonS3;
import com.hallym.rehab.domain.admin.dto.UploadFileDTO;
import com.hallym.rehab.domain.admin.dto.VideoRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface VideoService {
    String createVideo(VideoRequestDTO videoRequestDTO);
    String deleteVideo(Long vno);
    File convertMultipartFileToFile(MultipartFile multipartFile, String fileName);
    UploadFileDTO uploadFileToS3(MultipartFile videoFile, MultipartFile jsonFile);
    void deleteFileFromS3(String guideVideoObjectPath, String jsonObjectPath);
    void setAcl(AmazonS3 s3, String ObjectPath);
    void clearAllVideoAndJson();
}
