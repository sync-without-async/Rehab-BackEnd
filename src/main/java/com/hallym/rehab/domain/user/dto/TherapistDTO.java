package com.hallym.rehab.domain.user.dto;

import com.hallym.rehab.domain.user.entity.Staff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TherapistDTO {

    private String mid;

    private String name;

    private String hospital;

    private String department;

    public TherapistDTO(Staff staff) {
        this.mid = staff.getMid();
        this.name = staff.getName();
        this.hospital = staff.getHospital();
        this.department = staff.getDepartment();
    }
}
