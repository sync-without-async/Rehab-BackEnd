package com.hallym.rehab.domain.program.controller;

import com.hallym.rehab.domain.program.dto.program.ProgramDetailResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramHistoryDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramListResponseDTO;
import com.hallym.rehab.domain.program.service.ProgramService;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/program")
@RequiredArgsConstructor
public class ProgramUserController {

    private final ProgramService programService;

    @GetMapping("/list")
    public PageResponseDTO<ProgramListResponseDTO> getProgramList(PageRequestDTO pageRequestDTO) {
        return programService.getProgramList(pageRequestDTO);
    }

    @GetMapping("/{pno}")
    public ProgramDetailResponseDTO getProgramOne(@PathVariable Long pno, String mid) {
        return programService.getProgramOne(pno, mid);
    }

    @PostMapping("/addHistory/{pno}")
    public ResponseEntity<String> takeAProgram(@PathVariable Long pno, @RequestBody ProgramHistoryDTO programHistoryDTO) {
        String mid = programHistoryDTO.getMid();
        String result = programService.addProgramHistory(pno, mid);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{pno}")
    public ResponseEntity<String> cancelProgram(@PathVariable Long pno, @RequestBody ProgramHistoryDTO programHistoryDTO) {
        String mid = programHistoryDTO.getMid();
        String result = programService.cancelProgram(pno, mid);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history/{pno}")
    public ProgramHistoryDTO getHistoryOne(@PathVariable Long pno, @RequestBody ProgramHistoryDTO programHistoryDTO) {
        String mid = programHistoryDTO.getMid();
        return programService.getProgramHistoryOne(pno, mid);
    }

    @GetMapping("/history/list")
    public List<ProgramListResponseDTO> getProgramHistoryList(@RequestBody ProgramHistoryDTO programHistoryDTO) {
        String mid = programHistoryDTO.getMid();
        return programService.getProgramHistoryList(mid);
    }
}
