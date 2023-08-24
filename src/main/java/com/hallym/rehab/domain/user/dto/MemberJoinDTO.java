package com.hallym.rehab.domain.user.dto;

import lombok.Data;

@Data
public class MemberJoinDTO {

    private String mid;

    private String password;

    private String name;

    private int age;

    private String sex;

    private String phone;

}
