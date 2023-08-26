package com.hallym.rehab.domain.program.controller;

import com.hallym.rehab.domain.program.dto.program.ProgramDetailResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramMainResponseDTO;
import com.hallym.rehab.domain.program.dto.video.VideoResponseDTO;
import com.hallym.rehab.domain.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/program")
@RequiredArgsConstructor
public class ProgramUserController {

    private final ProgramService programService;

    @GetMapping()
    public List<ProgramMainResponseDTO> getProgramList(
            @RequestParam(value = "", required = false) String category,
            @RequestParam(value = "", required = false) String position) {

        log.info(category);
        log.info(position);
        return null;
    }

    @GetMapping("/{pno}")
    public ProgramDetailResponseDTO getProgramOne(@PathVariable Long pno) {

        return null;
    }

}
