package com.hallym.rehab.domain.room.entity;

import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.reservation.entity.Reservation;
import com.hallym.rehab.domain.room.dto.RoomResponseDTO;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

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
    @JoinColumn(name = "staff_id", referencedColumnName = "mid")
    private Staff staff;

    @OneToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "mid")
    private Patient patient;

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
                .staff_id(this.getStaff().getMid())
                .patient_id(this.getPatient().getMid())
                .build();
    }
}
