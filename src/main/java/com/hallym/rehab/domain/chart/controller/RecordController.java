package com.hallym.rehab.domain.chart.controller;

import com.hallym.rehab.domain.chart.dto.RecordDTO;
import com.hallym.rehab.domain.chart.service.RecordService;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
@Slf4j
public class RecordController {

    private final RecordService recordService;

    @GetMapping("/{record_no}")
    public RecordDTO getRecordOne(@PathVariable Long record_no) {

        return recordService.getRecordDetails(record_no);
    }

    @PostMapping("/register/{vno}")
    public void registerRecord(@RequestBody RecordDTO recordDTO, @PathVariable Long vno) {

        recordService.registerRecordDetails(recordDTO, vno);
    }

    @GetMapping("/list")
    public PageResponseDTO<RecordDTO> getRecordList(PageRequestDTO pageRequestDTO){

        return recordService.getRecordList(pageRequestDTO);
    }

}
