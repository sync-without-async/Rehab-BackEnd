package com.hallym.rehab.domain.user.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "staff")
public class StaffImage {

    @Id
    private String profileUrl;

    @OneToOne
    private Staff staff;

    public void changeStaff(Staff staff) {
        this.staff = staff;
    }

}