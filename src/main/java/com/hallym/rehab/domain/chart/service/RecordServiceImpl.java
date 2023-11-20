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


    /**
     * record_no로 조회한 특정 진료 기록을 DTO로 변환하여 전달한다.
     * @param record_no
     * @return RecordDTO
     */
    @Override
    public RecordDTO getRecordDetails(Long record_no) {

        Optional<Record> result = recordRepository.findById(record_no);

        Record record = result.orElseThrow();

        return RecordDTO.builder()
                .record_no(record.getRecord_no())
                .schedule(record.getSchedule())
                .treatmentRecord(record.getTreatmentRecord())
                .exerciseRequest(record.getExerciseRequest())
                .regDate(record.getRegDate().toLocalDate())
                .build();
    }

    /**
     * cno로 검색한 특정 환자차트에 신규 진료 기록을 등록하여 DTO로 반환한다.
     * @param recordDTO
     * @param cno
     * @return "진료기록 등록 성공" or "진료기록 등록에 실패했습니다"
     */
    @Override
    public String registerRecordDetails(RecordDTO recordDTO, Long cno) {
        Chart chart = chartRepository.findById(cno).orElseThrow();

        log.info("Registering---" + chart);

        Record record = Record.builder()
                .schedule(recordDTO.getSchedule())
                .treatmentRecord(recordDTO.getTreatmentRecord())
                .exerciseRequest(recordDTO.getExerciseRequest())
                .chart(chart)
                .build();

        try {
            recordRepository.save(record);
            return "진료기록 등록 성공";
        } catch (Exception e) {
            log.error("Failed to save record details.", e);
            throw new RuntimeException("진료기록 등록에 실패했습니다");
        }
    }


    /**
     * 환자의 진료 기록 목록을 조회한다.
     * @param pageRequestDTO
     * @return PageResponseDTO<RecordDTO>
     */
    @Override
    public PageResponseDTO<RecordDTO> getRecordList(PageRequestDTO pageRequestDTO) {
        return null;
    }

}
