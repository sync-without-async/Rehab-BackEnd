package com.hallym.rehab.domain.room.dto;

import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadAudioDTO {
    private String audioObjectPath;
    private String audioURL;
}
