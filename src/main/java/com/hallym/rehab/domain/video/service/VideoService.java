package com.hallym.rehab.domain.video.service;

import com.amazonaws.services.s3.AmazonS3;
import com.hallym.rehab.domain.video.dto.UploadFileDTO;
import com.hallym.rehab.domain.video.dto.pagedto.VideoPageRequestDTO;
import com.hallym.rehab.domain.video.dto.VideoRequestDTO;
import com.hallym.rehab.domain.video.dto.VideoResponseDTO;
import com.hallym.rehab.domain.video.dto.pagedto.VideoPageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface VideoService {
    VideoPageResponseDTO<VideoResponseDTO> getVideoList(VideoPageRequestDTO requestDTO);
    String createVideo(VideoRequestDTO videoRequestDTO);
    String deleteVideo(Long vno);
    File convertMultipartFileToFile(MultipartFile multipartFile, String fileName);
    UploadFileDTO uploadFileToS3(MultipartFile videoFile, MultipartFile jsonFile);
    void deleteFileFromS3(String guideVideoObjectPath, String jsonObjectPath);
    void setAcl(AmazonS3 s3, String ObjectPath);
    void clearAllVideoAndJson();
}
