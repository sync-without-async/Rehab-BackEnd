package com.hallym.rehab.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StaffRole {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"), ;

    private String value;

    StaffRole(String value) {
        this.value = value;
    }

}
