package com.hallym.rehab.global.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@ToString
public class APIUserDTO extends User {

    private String mid;

    private String password;

    private String name;

    private String phone;

    public APIUserDTO(String username, String password, String name, String phone, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.mid = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

}