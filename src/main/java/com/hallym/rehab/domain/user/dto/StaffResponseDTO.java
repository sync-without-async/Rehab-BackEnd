package com.hallym.rehab.domain.user.dto;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffResponseDTO {

    private String mid;

    private String name;

    private String hospital;

    private String department;

    private String email;

    private String phone;

    @NonNull
    private String staffRole;

    private String fileName;

}
