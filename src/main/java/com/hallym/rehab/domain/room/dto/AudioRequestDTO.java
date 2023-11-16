package com.hallym.rehab.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AudioRequestDTO {
    private UUID rno;
    private Boolean is_patient;
    private MultipartFile audioFile;
}
