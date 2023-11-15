package com.hallym.rehab.domain.room.dto;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RoomResponseDTO {
    private UUID rno;
    private String staff_id;
    private String patient_id;
}
