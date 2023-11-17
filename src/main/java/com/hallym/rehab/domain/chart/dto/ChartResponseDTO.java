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

    private String phone;

    private String sex;

    private LocalDate birth;

    private String patient_id; // 환자 ID

    private String patient_name; //환자성함

    private String doctor_name; // 담당의사

    private String therapist_name; // 담당재활치료사

    private double metrics_rate;

    private LocalDate regDate;

    private List<RecordDTO> medicalRecords;

}
