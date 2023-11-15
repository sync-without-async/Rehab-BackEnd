package com.hallym.rehab.domain.chart.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChartResponseDTO {

    private long cno;

    private String cd; //질병분류기호

    private String patientName; //환자성함

    private String phone;

    private String sex;

    private LocalDate birth;

    private String doctor_id; // 담당의사

    private String therapist_id; // 담당재활치료사

    private List<RecordDTO> medicalRecords;

    private List<OnlineRecordDTO> OnlineRecords;
}
