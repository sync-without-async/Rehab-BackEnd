package com.hallym.rehab.domain.reservation.entity;


import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.reservation.dto.ReservationResponseByAdminDTO;
import com.hallym.rehab.domain.reservation.dto.ReservationResponseByUserDTO;
import com.hallym.rehab.domain.room.entity.Room;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @ColumnDefault("false")
    private boolean is_deleted;

    public void setDelete(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public static ReservationResponseByAdminDTO toAdminDTO(Reservation reservation) {
        return ReservationResponseByAdminDTO.builder()
                    .userName(reservation.getUser().getName())
                    .userId(reservation.getUser().getMid())
                    .rno(reservation.getRoom().getRno())
                    .date(reservation.getDate())
                    .index(reservation.getIndex())
                    .build();
    }

    public static ReservationResponseByUserDTO toUserDTO(Reservation reservation) {
        return ReservationResponseByUserDTO.builder()
                .adminName(reservation.getAdmin().getName())
                .rno(reservation.getRoom().getRno())
                .rvno(reservation.getRvno())
                .date(reservation.getDate())
                .index(reservation.getIndex())
                .build();
    }
}
