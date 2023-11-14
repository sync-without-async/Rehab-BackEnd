package com.hallym.rehab.domain.room.entity;

import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.reservation.entity.Reservation;
import com.hallym.rehab.domain.room.dto.RoomResponseDTO;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.mapping.Join;

import javax.persistence.*;
import java.util.UUID;
import org.springframework.lang.Nullable;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)") // UUID를 이진 형태로 저장하도록 지정
    private UUID rno;

    @OneToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "mid")
    private Admin admin;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "mid")
    private Member user;

    @OneToOne
    @JoinColumn(name = "reservation_id", referencedColumnName = "rvno")
    private Reservation reservation;

    @OneToOne
    private Audio audio;

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public RoomResponseDTO toRoomResponseDTO() {
        return RoomResponseDTO.builder()
                .rno(this.getRno())
                .admin_id(this.getAdmin().getMid())
                .user_id(this.getUser().getMid())
                .build();
    }
}
