package com.hallym.rehab.domain.program.dto.upload;

import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileDTO {
    private String guideVideoURL;
    private String jsonURL;
    private String guideVideoObjectPath;
    private String jsonObjectPath;
}
