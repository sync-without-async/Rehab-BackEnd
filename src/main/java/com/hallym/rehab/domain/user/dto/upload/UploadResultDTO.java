package com.hallym.rehab.domain.user.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadResultDTO { //파일이 여러 개인 경우 여러 정보를 반환해야하기 때문에 별도의 DTO 생성

    private String profileUrl;
}
