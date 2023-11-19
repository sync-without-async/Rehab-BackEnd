package com.hallym.rehab.domain.video.service;

import com.amazonaws.services.s3.AmazonS3;
import com.hallym.rehab.domain.program.dto.ProgramResponseDTO;
import com.hallym.rehab.global.s3.dto.UploadVideoResponseDTO;
import com.hallym.rehab.domain.video.dto.VideoDetailResponseDTO;
import com.hallym.rehab.domain.video.dto.pagedto.VideoPageRequestDTO;
import com.hallym.rehab.domain.video.dto.VideoRequestDTO;
import com.hallym.rehab.domain.video.dto.VideoResponseDTO;
import com.hallym.rehab.domain.video.dto.pagedto.VideoPageResponseDTO;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface VideoService {
    VideoPageResponseDTO<VideoResponseDTO> getVideoListByAdmin(VideoPageRequestDTO requestDTO);

    Pair<String, VideoPageResponseDTO<ProgramResponseDTO>> getVideoListByUser(VideoPageRequestDTO requestDTO,
                                                                              String userId);

    VideoDetailResponseDTO getVideo(Long vno);

    String createVideo(VideoRequestDTO videoRequestDTO);

    String deleteVideo(Long vno);
}
