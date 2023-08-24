package com.hallym.rehab.domain.program.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Position {

    STANDING,
    SITTING,
    LYING;

    @JsonCreator
    public static Position from(String s){
        return Position.valueOf(s);
    }
}
