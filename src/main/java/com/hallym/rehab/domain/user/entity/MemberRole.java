package com.hallym.rehab.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    DOCTOR("ROLE_DOCTOR"),
    THERAPIST ("ROLE_THERAPIST"),
    PATIENT ("ROLE_PATIENT")
    ;

    private String value;

    MemberRole(String value) {
        this.value = value;
    }

}
