package com.hallym.rehab.domain.room.service;

import com.amazonaws.services.s3.AmazonS3;
import com.hallym.rehab.domain.room.dto.AudioRequestDTO;
import com.hallym.rehab.domain.room.dto.UploadAudioDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface AudioService {
    String registerAudio(AudioRequestDTO audioRequestDTO);
    UploadAudioDTO uploadFileToS3(MultipartFile audioFile);
    File convertMultipartFileToFile(MultipartFile multipartFile, String fileName);
    void setAcl(AmazonS3 s3, String objectPath);
}
