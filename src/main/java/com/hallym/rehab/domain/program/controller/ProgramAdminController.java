package com.hallym.rehab.domain.program.controller;

import com.hallym.rehab.domain.program.dto.program.ProgramRequestDTO;
import com.hallym.rehab.domain.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth/program")
@RequiredArgsConstructor
public class ProgramAdminController {

    private final ProgramService programService;

//    @PreAuthorize("authentication.principal.username == #passwordChangeDTO.mid or hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public Map<String, Long> createProgram(@ModelAttribute ProgramRequestDTO programRequestDTO) {
        Long result = programService.createProgram(programRequestDTO);
        return Map.of("Program number : ", result);
    }

    @PutMapping("/modify/{pno}")
    public ResponseEntity<String> modifyProgram(@PathVariable Long pno,
                                                @ModelAttribute ProgramRequestDTO programRequestDTO) {
        String result = programService.modifyProgramOne(pno, programRequestDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{pno}")
    public ResponseEntity<String> deleteProgram(@PathVariable Long pno) {
        String result = programService.deleteProgramOne(pno);
        return ResponseEntity.ok(result);
    }


}
