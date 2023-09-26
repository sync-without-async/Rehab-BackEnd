package com.hallym.rehab.domain.video.controller;

import com.hallym.rehab.domain.video.dto.VideoRequestDTO;
import com.hallym.rehab.domain.video.dto.pagedto.VideoPageRequestDTO;
import com.hallym.rehab.domain.video.dto.VideoResponseDTO;
import com.hallym.rehab.domain.video.dto.pagedto.VideoPageResponseDTO;
import com.hallym.rehab.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @GetMapping("/list")
    public VideoPageResponseDTO<VideoResponseDTO> getList(VideoPageRequestDTO pageRequestDTO) {
        return videoService.getVideoList(pageRequestDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createVideo(VideoRequestDTO videoRequestDTO) {
        String result = videoService.createVideo(videoRequestDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{vno}")
    public ResponseEntity<String> deleteVideo(@PathVariable Long vno) {
        String result = videoService.deleteVideo(vno);
        return ResponseEntity.ok(result);
    }
}
