package com.hallym.rehab.domain.video.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.program.dto.ProgramResponseDTO;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.repository.ProgramDetailRepository;
import com.hallym.rehab.domain.program.repository.ProgramRepository;
import com.hallym.rehab.global.s3.S3Util;
import com.hallym.rehab.global.s3.dto.UploadVideoResponseDTO;
import com.hallym.rehab.domain.video.dto.VideoDetailResponseDTO;
import com.hallym.rehab.domain.video.dto.pagedto.VideoPageRequestDTO;
import com.hallym.rehab.domain.video.dto.VideoRequestDTO;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.domain.video.dto.VideoResponseDTO;
import com.hallym.rehab.domain.video.dto.pagedto.VideoPageResponseDTO;
import com.hallym.rehab.domain.video.entity.Video;
import com.hallym.rehab.domain.video.repository.VideoRepository;
import com.hallym.rehab.global.s3.S3Client;
import com.hallym.rehab.global.util.AWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final S3Util s3Util;
    private final StaffRepository staffRepository;
    private final VideoRepository videoRepository;
    private final ProgramRepository programRepository;
    private final ProgramDetailRepository programDetailRepository;

    @Override
    public VideoPageResponseDTO<VideoResponseDTO> getVideoListByAdmin(VideoPageRequestDTO requestDTO) {
        Page<VideoResponseDTO> result = videoRepository.search(requestDTO);

        return VideoPageResponseDTO.<VideoResponseDTO>withAll()
                .pageRequestDTO(requestDTO)
                .dtoList(result.getContent())
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public Pair<String, VideoPageResponseDTO<ProgramResponseDTO>> getVideoListByUser(VideoPageRequestDTO requestDTO,
                                                                                     String userId) {
        Program program = programRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("not found program for userId : " + userId));

        Pageable pageable = requestDTO.getPageable();

        Page<ProgramResponseDTO> result = programDetailRepository.findPageByProgram(program, pageable);

        VideoPageResponseDTO<ProgramResponseDTO> responseDTO = VideoPageResponseDTO.<ProgramResponseDTO>withAll()
                .pageRequestDTO(requestDTO)
                .dtoList(result.getContent())
                .total((int) result.getTotalElements())
                .build();

        return Pair.of(program.getDescription(), responseDTO);
    }

    @Override
    public VideoDetailResponseDTO getVideo(Long vno) {
        Video video = videoRepository.findById(vno)
                .orElseThrow(() -> new NotFoundException("video not found for id -> " + vno));

        return video.toDetailDTO();
    }

    @Override
    public String createVideo(VideoRequestDTO videoRequestDTO) {
        Staff staff = staffRepository.findById(videoRequestDTO.getStaff_id())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디 입니다."));

        MultipartFile[] files = videoRequestDTO.getFiles();
        MultipartFile videoFile = files[0];
        MultipartFile jsonFile = files[1];
        UploadVideoResponseDTO uploadVideoResponseDTO = s3Util.uploadVideoAndJson(videoFile, jsonFile);

        Video video = videoRequestDTO.toVideo(staff, uploadVideoResponseDTO);
        videoRepository.save(video);

        return "Success create Video";
    }

    @Override
    public String deleteVideo(Long vno) {
        Optional<Video> byId = videoRepository.findById(vno);
        if (byId.isEmpty()) {
            return "Video not found for Id : " + vno;
        }

        Video video = byId.get();
        String videoPath = video.getVideoPath();
        String jsonPath = video.getJsonPath();

        s3Util.deleteFileFromS3(videoPath);
        s3Util.deleteFileFromS3(jsonPath);

        videoRepository.delete(video);

        return "Success delete Video";
    }
}
