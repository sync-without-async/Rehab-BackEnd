package com.hallym.rehab.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"), ;

    private String value;

    MemberRole(String value) {
        this.value = value;
    }

}
