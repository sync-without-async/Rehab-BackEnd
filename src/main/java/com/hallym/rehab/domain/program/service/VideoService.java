package com.hallym.rehab.domain.program.service;

import java.util.*;

import com.amazonaws.services.s3.AmazonS3;
import com.hallym.rehab.domain.program.dto.upload.UploadFileDTO;
import com.hallym.rehab.domain.program.dto.video.*;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.Video;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.stream.Collectors;

public interface VideoService {

    String createVideo(Long pno, Long ord, VideoRequestDTO videoRequestDTO);
    String deleteVideo(Long pno, Long ord);
    VideoResponseDTO getVideoList(Long pno);
    String saveMetrics(Long vno, MetricsRequestDTO metricsRequestDTO);
    String changeVideoOrd(Long pno, ChangeOrdRequestDTO changeOrdRequestDTO);
    UploadFileDTO uploadFileToS3(MultipartFile videoFile, MultipartFile jsonFile, Program program);
    File convertMultipartFileToFile(MultipartFile multipartFile, String fileName);
    void setAcl(AmazonS3 s3, String ObjectPath);
    void deleteFileFromS3(String guideVideoObjectPath, String jsonObjectPath);

    default VideoResponseDTO videoToVideoResponseDTO(List<Video> videoList, Program program) {

        List<VideoUrl> videoUrl = videoList.stream()
                .sorted(Comparator.comparing(Video::getOrd))
                .map(video -> new VideoUrl(video.getVno(), video.getGuideVideoURL()))
                .collect(Collectors.toList());

        return VideoResponseDTO.builder()
                .pno(program.getPno())
                .programTitle(program.getProgramTitle())
                .description(program.getDescription())
                .category(program.getCategory())
                .position(program.getPosition())
                .vno_videoUrl(videoUrl)
                .build();
    }
}
