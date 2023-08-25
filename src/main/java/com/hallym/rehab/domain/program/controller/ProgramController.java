package com.hallym.rehab.domain.program.controller;

import com.hallym.rehab.domain.program.dto.ProgramRequestDTO;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/program")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;

//    @PreAuthorize("authentication.principal.username == #passwordChangeDTO.mid or hasRole('ROLE_ADMIN')")
    @PostMapping("/auth/create")
    public ResponseEntity<String> createProgram(@RequestParam("videoFile") MultipartFile videoFile,
                                                @RequestParam("jsonFile") MultipartFile jsonFile,
                                                @ModelAttribute ProgramRequestDTO programRequestDTO) {

        if (videoFile.isEmpty() || jsonFile.isEmpty()) return ResponseEntity.badRequest().body("Please select files to upload");

        String result = programService.createProgram(programRequestDTO, videoFile, jsonFile);
//        programService.uploadFileToS3(videoFile, jsonFile, program);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/auth/modify/{pno}")
    public ResponseEntity<String> modifyProgram(@PathVariable Long pno,
                                                @RequestParam("videoFile") MultipartFile videoFile,
                                                @RequestParam("jsonFile") MultipartFile jsonFile,
                                                @ModelAttribute ProgramRequestDTO programRequestDTO) {

        if (videoFile.isEmpty() || jsonFile.isEmpty()) return ResponseEntity.badRequest().body("Please select files to upload");

        programService.modifyProgramOne(pno, programRequestDTO, videoFile, jsonFile);

        return ResponseEntity.ok("Program successfully modified and files uploaded");
    }

    @DeleteMapping("/auth/delete/{pno}")
    public ResponseEntity<String> deleteProgram(@PathVariable Long pno) {
        String result = programService.deleteProgramOne(pno);
        return ResponseEntity.ok(result);
    }
}
