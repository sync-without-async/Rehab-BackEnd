package com.hallym.rehab.domain.program.controller;

import com.hallym.rehab.domain.program.dto.video.MetricsRequestDTO;
import com.hallym.rehab.domain.program.dto.video.VideoResponseDTO;
import com.hallym.rehab.domain.program.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoUserController {

    private final VideoService videoService;

    @GetMapping("/{pno}") // pno로 Video List 반환
    public VideoResponseDTO getVideoList(@PathVariable Long pno) {
        return videoService.getVideoList(pno);
    }

//    @PreAuthorize("authentication.principal.username == #metricsRequestDTO.mid or hasRole('ROLE_ADMIN')")
    @PutMapping("/modify/metrics/{vno}")
    public ResponseEntity<String> saveMetrics(@PathVariable Long vno,
                                             @RequestBody MetricsRequestDTO metricsRequestDTO) {
        String result = videoService.saveMetrics(vno, metricsRequestDTO);
        return ResponseEntity.ok(result);
    }
}