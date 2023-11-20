package com.hallym.rehab.domain.user.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffImageDTO {

    @NonNull
    private String uuid;

    @NonNull
    private String fileName;

}
