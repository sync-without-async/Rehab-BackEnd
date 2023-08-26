package com.hallym.rehab.domain.program.controller;

import com.hallym.rehab.domain.program.dto.video.MatrixRequestDTO;
import com.hallym.rehab.domain.program.dto.video.VideoResponseDTO;
import com.hallym.rehab.domain.program.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoUserController {

    private final VideoService videoService;

    @GetMapping("/{pno}") // pno로 Video List 반환
    public List<VideoResponseDTO> getVideo(@PathVariable Long pno) {

        return null;
    }

    @PutMapping("/modify/matrix/{vno}")
    public ResponseEntity<String> saveMatrix(@PathVariable Long vno,
                                             @ModelAttribute MatrixRequestDTO matrixRequestDTO) {
        String result = videoService.saveMatrix(vno, matrixRequestDTO);
        return ResponseEntity.ok(result);
    }
}