package com.hallym.rehab.domain.user.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {

    @NonNull
    private String mid;

    @NonNull
    private String name;

    @NonNull
    private LocalDate birth;

    @NonNull
    private String sex;

    @NonNull
    private String phone;
}
