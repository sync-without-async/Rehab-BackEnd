package com.hallym.rehab.domain.chart.service;

import com.hallym.rehab.domain.chart.dto.RecordDTO;
import com.hallym.rehab.domain.chart.entity.Chart;
import com.hallym.rehab.domain.chart.entity.Record;
import com.hallym.rehab.domain.chart.repository.ChartRepository;
import com.hallym.rehab.domain.chart.repository.RecordRepository;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final ChartRepository chartRepository;


    @Override
    public RecordDTO getRecordDetails(Long record_no) {

        Optional<Record> result = recordRepository.findById(record_no);

        Record record = result.orElseThrow();

        return RecordDTO.builder()
                .record_no(record.getRecord_no())
                .schedule(record.getSchedule())
                .treatmentRecord(record.getTreatmentRecord())
                .exerciseRequest(record.getExerciseRequest())
                .build();
    }

    @Override
    public void registerRecordDetails(RecordDTO recordDTO, Long cno) {

        Chart chart = chartRepository.findById(cno).orElseThrow();

        log.info("Registering---" + chart);

        Record record = Record.builder()
                .schedule(recordDTO.getSchedule())
                .treatmentRecord(recordDTO.getTreatmentRecord())
                .exerciseRequest(recordDTO.getExerciseRequest())
                .chart(chart)
                .build();

        recordRepository.save(record);

    }

    @Override
    public PageResponseDTO<RecordDTO> getRecordList(PageRequestDTO pageRequestDTO) {
        return null;
    }

}
