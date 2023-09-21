package com.hallym.rehab.domain.reservation.entity;


import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rvno;

    @OneToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "mid")
    private Admin admin;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "mid")
    private Member user;

    private String content;
}
