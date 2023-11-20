package com.hallym.rehab.domain.user.dto;

import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyStaffDetailDTO {

    @NonNull
    private StaffResponseDTO doctor_info;

    @NonNull
    private StaffResponseDTO therapist_info;

}
