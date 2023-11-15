package com.hallym.rehab.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StaffRole {

    DOCTOR("ROLE_DOCTOR"),
    THERAPIST ("ROLE_THERAPIST"), ;

    private String value;

    StaffRole(String value) {
        this.value = value;
    }

}
