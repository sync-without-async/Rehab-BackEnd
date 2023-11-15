package com.hallym.rehab.domain.chart.dto;

import lombok.*;

import java.time.LocalDate;

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

    private String medicalRecord; //진료 기록

    private String exerciseRequest; //운동요청서
}
