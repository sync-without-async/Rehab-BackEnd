package com.hallym.rehab.domain.reservation.entity;


import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.room.entity.Room;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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

    @OneToOne
    @JoinColumn(name = "room_id", referencedColumnName = "rno")
    private Room room;

    private LocalDate date;

    @Column(name = "time_index")
    private int index;

    private String content;
}
