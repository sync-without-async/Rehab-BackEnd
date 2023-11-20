package com.hallym.rehab.domain.user.dto;

import com.hallym.rehab.domain.user.entity.Staff;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TherapistDTO {

    @NonNull
    private String mid;

    @NonNull
    private String name;

    @NonNull
    private String hospital;

    @NonNull
    private String department;

//    @NonNull
//    private String staffRole;
//
//    @NonNull
//    private String phone;
//
//    private String fileName;

    public TherapistDTO(Staff staff) {
        this.mid = staff.getMid();
        this.name = staff.getName();
        this.hospital = staff.getHospital();
        this.department = staff.getDepartment();
    }
}
