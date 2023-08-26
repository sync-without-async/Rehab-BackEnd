package com.hallym.rehab.domain.program.controller;

import com.hallym.rehab.domain.program.dto.program.ProgramDetailResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramMainResponseDTO;
import com.hallym.rehab.domain.program.dto.video.VideoResponseDTO;
import com.hallym.rehab.domain.program.service.ProgramService;
import com.hallym.rehab.domain.program.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoUserController {

    private final VideoService videoService;

    @GetMapping("/video/{pno}") // pno로 Video List 반환
    public List<VideoResponseDTO> getVideo(@PathVariable Long pno) {

        return null;
    }
}