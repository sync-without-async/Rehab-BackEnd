package com.hallym.rehab.domain.reservation.entity;


import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.reservation.dto.ReservationResponseByStaffDTO;
import com.hallym.rehab.domain.reservation.dto.ReservationResponseByPatientDTO;
import com.hallym.rehab.domain.room.entity.Room;
import com.hallym.rehab.domain.user.entity.Patient;
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
    @JoinColumn(name = "staff_id", referencedColumnName = "mid")
    private Staff staff;

    @OneToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "mid")
    private Patient patient;

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

    public static ReservationResponseByStaffDTO toAdminDTO(Reservation reservation) {

        String summary;

        if (reservation.getRoom().getAudio().getSummary() == null) {
            summary = "";
        } else {
            summary = reservation.getRoom().getAudio().getSummary();
        }

        return ReservationResponseByStaffDTO.builder()
                .patientName(reservation.getPatient().getName())
                .patientId(reservation.getPatient().getMid())
                .rno(reservation.getRoom().getRno())
                .rvno(reservation.getRvno())
                .date(reservation.getDate())
                .index(reservation.getIndex())
                .summary(summary)
                .content(reservation.getContent())
                .build();
    }

    public static ReservationResponseByPatientDTO toUserDTO(Reservation reservation) {

        String summary;

        if (reservation.getRoom().getAudio().getSummary() == null) {
            summary = "";
        } else {
            summary = reservation.getRoom().getAudio().getSummary();
        }

        return ReservationResponseByPatientDTO.builder()
                .staffName(reservation.getStaff().getName())
                .rno(reservation.getRoom().getRno())
                .rvno(reservation.getRvno())
                .date(reservation.getDate())
                .index(reservation.getIndex())
                .content(reservation.getContent())
                .summary(summary)
                .build();
    }
}
