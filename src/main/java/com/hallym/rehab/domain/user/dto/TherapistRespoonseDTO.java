package com.hallym.rehab.domain.user.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TherapistRespoonseDTO {

    @NonNull
    private String mid;

    @NonNull
    private String name;

    @NonNull
    private String hospital;

    @NonNull
    private String department;
}
