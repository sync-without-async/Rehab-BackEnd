package com.hallym.rehab.domain.chart.service;

import com.hallym.rehab.domain.chart.dto.RecordDTO;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;

public interface RecordService {

    RecordDTO getRecordDetails(Long record_no);

    String registerRecordDetails(RecordDTO recordDTO, Long cno);

//    void modifyRecordDetails(RecordDTO recordDTO);

//    void removeRecordDetails(Long record_no);

    PageResponseDTO<RecordDTO> getRecordList(PageRequestDTO pageRequestDTO);
}
