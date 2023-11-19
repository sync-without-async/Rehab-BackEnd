package com.hallym.rehab.domain.video.controller;

import com.hallym.rehab.domain.program.dto.ProgramResponseDTO;
import com.hallym.rehab.domain.video.dto.VideoDetailResponseDTO;
import com.hallym.rehab.domain.video.dto.VideoRequestDTO;
import com.hallym.rehab.domain.video.dto.pagedto.VideoPageRequestDTO;
import com.hallym.rehab.domain.video.dto.VideoResponseDTO;
import com.hallym.rehab.domain.video.dto.pagedto.VideoPageResponseDTO;
import com.hallym.rehab.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @GetMapping("/list/{patient_id}")
    public Pair<String, VideoPageResponseDTO<ProgramResponseDTO>> getListByUser(VideoPageRequestDTO pageRequestDTO,
                                                                                @PathVariable("patient_id") String patient_id) {
        return videoService.getVideoListByUser(pageRequestDTO, patient_id);
    }

    @GetMapping("/list")
    public VideoPageResponseDTO<VideoResponseDTO> getListByAdmin(VideoPageRequestDTO pageRequestDTO) {
        return videoService.getVideoListByAdmin(pageRequestDTO);
    }

    @GetMapping("/{vno}")
    public VideoDetailResponseDTO getOne(@PathVariable("vno") Long vno) {
        return videoService.getVideo(vno);
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
