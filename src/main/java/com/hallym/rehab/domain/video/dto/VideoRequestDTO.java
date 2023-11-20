package com.hallym.rehab.domain.video.dto;

import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.video.entity.Tag;
import com.hallym.rehab.domain.video.entity.Video;
import com.hallym.rehab.global.s3.dto.UploadVideoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoRequestDTO {
    private String staff_id;
    private String title;
    private String description; // 동작 설명
    private Tag tag; // 동작 태그
    private Long frame; // AI 에서 쓸 영상 프레임
    private double playTime; // FRONT 에서 쓸 영상 시간
    @Builder.Default
    private MultipartFile[] files = new MultipartFile[2];

    public Video toVideo(Staff staff, UploadVideoResponseDTO uploadVideoResponseDTO) {
        return Video.builder()
                .staff(staff)
                .title(title)
                .description(description)
                .tag(tag)
                .frame(frame)
                .playTime(playTime)
                .videoURL(uploadVideoResponseDTO.getVideoURL())
                .jsonURL(uploadVideoResponseDTO.getJsonURL())
                .videoPath(uploadVideoResponseDTO.getVideoPath())
                .jsonPath(uploadVideoResponseDTO.getJsonPath())
                .thumbnailURL(uploadVideoResponseDTO.getThumbnailURL())
                .thumbnailPath(uploadVideoResponseDTO.getThumbnailPath())
                .build();
    }
}
