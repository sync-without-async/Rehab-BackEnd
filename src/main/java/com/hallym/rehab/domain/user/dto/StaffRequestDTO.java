package com.hallym.rehab.domain.user.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffRequestDTO {

    @NonNull
    private String mid;

    @NonNull
    private String name;

    @NonNull
    private String password;

    @NonNull
    private String hospital;

    @NonNull
    private String department;

    @NonNull
    private String email;

    @NonNull
    private String phone;

    @NonNull
    private String staffRole;

    private String fileName;

}
