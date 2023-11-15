package com.hallym.rehab.domain.user.entity;

import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Patient extends BaseTimeEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String mid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    private LocalDate birth;

    private String sex;

    private String phone;

    @ColumnDefault("false")
    private boolean is_deleted;

    public void changePassword(String password) {
        this.password = password;
    }

    public void addUser(String mid, String password, String name, String phone) {
        this.mid = mid;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }
}
