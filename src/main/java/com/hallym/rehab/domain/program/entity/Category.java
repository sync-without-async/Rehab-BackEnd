package com.hallym.rehab.domain.program.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Category {

    UPPER,//상지
    LOWER; //하지

    @JsonCreator
    public static Category from(String s){
        return Category.valueOf(s);
    }
}
