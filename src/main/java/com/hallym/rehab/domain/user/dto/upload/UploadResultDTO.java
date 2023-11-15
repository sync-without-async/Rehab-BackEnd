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

    private String uuid; //업로드 된 파일의 UUID

    private String fileName; //파일 이름

    public String getLink() { //JSON 처리 시 첨부 파일 경로 처리를 위한 link 속성으로 자동 처리

        return "s_" + uuid + "_" + fileName;
    }
}
