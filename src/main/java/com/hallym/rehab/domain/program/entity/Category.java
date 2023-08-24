package com.hallym.rehab.domain.program.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Category {

    SHOULDERS,//어깨
    ARMS, //팔
    KNEES, //무릎
    THIGHS; //허벅지

    @JsonCreator
    public static Category from(String s){
        return Category.valueOf(s);
    }
}
