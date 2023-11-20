package com.hallym.rehab.domain.user.dto;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffResponseDTO {

    @NonNull
    private String mid;

    @NonNull
    private String name;

    @NonNull
    private String hospital;

    @NonNull
    private String department;

    private String email;

    private String phone;

    @NonNull
    private String staffRole;

    private String fileName;

}
