package com.hallym.rehab.domain.chart.dto;

import com.hallym.rehab.domain.chart.entity.Record;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordDTO {

    private long record_no;
    private LocalDate schedule; //외래 일정
    private String treatmentRecord; //진료 기록
    private String exerciseRequest; //운동요청서

    public static RecordDTO of(Record record) {
        return RecordDTO.builder()
                .schedule(record.getSchedule())
                .treatmentRecord(record.getTreatmentRecord())
                .exerciseRequest(record.getExerciseRequest())
                .record_no(record.getRecord_no())
                .build();
    }
}
