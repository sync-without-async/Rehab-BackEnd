package com.hallym.rehab.domain.program.controller;

import com.hallym.rehab.domain.program.dto.ProgramRequestDTO;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/program")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;

    @PostMapping("/create")
    public ResponseEntity<String> createProgram(@RequestParam("videoFile") MultipartFile videoFile,
                                                @RequestParam("jsonFile") MultipartFile jsonFile,
                                                @ModelAttribute ProgramRequestDTO programRequestDTO) {

        if (videoFile.isEmpty() || jsonFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select files to upload");
        }

        Program program = programService.createProgram(programRequestDTO);
        programService.uploadFileToS3(videoFile, jsonFile, program);

        return ResponseEntity.ok("Program successfully created and files uploaded");
    }
}
