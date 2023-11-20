package com.hallym.rehab.domain.user.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeDTO {

    @NonNull
    private String mid;

    @NonNull
    private String currentPassword;

    @NonNull
    private String newPassword;
}
