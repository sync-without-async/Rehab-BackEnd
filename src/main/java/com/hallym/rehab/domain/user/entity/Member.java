package com.hallym.rehab.domain.user.entity;

import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String mid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    private int age;

    private String sex;

    private String phone;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>(); //권한 정보

    @ColumnDefault("false")
    private boolean is_deleted;

    public void addRole(MemberRole memberRole){
        this.roleSet.add(memberRole);
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
