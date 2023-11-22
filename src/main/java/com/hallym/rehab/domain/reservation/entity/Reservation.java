package com.hallym.rehab.domain.reservation.entity;

import com.hallym.rehab.domain.reservation.dto.ReservationResponseByPatientDTO;
import com.hallym.rehab.domain.reservation.dto.ReservationResponseByStaffDTO;
import com.hallym.rehab.domain.user.entity.Staff;
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

    public ReservationResponseByStaffDTO toAdminDTO() {

        String summary;

        if (this.getRoom().getAudio().getSummary() == null) {
            summary = "";
        } else {
            summary = this.getRoom().getAudio().getSummary();
        }

        return ReservationResponseByStaffDTO.builder()
                .patientName(this.getPatient().getName())
                .patientId(this.getPatient().getMid())
                .rno(this.getRoom().getRno())
                .rvno(this.getRvno())
                .date(this.getDate())
                .index(this.getIndex())
                .summary(summary)
                .content(this.getContent())
                .is_deleted(this.is_deleted())
                .build();
    }

    public ReservationResponseByPatientDTO toUserDTO() {

        String summary;

        if (this.getRoom().getAudio().getSummary() == null) {
            summary = "";
        } else {
            summary = this.getRoom().getAudio().getSummary();
        }

        return ReservationResponseByPatientDTO.builder()
                .staffName(this.getStaff().getName())
                .rno(this.getRoom().getRno())
                .rvno(this.getRvno())
                .date(this.getDate())
                .index(this.getIndex())
                .content(this.getContent())
                .summary(summary)
                .role(this.getStaff().getRoleSet().toString())
                .hospital(this.staff.getHospital())
                .profileUrl(this.staff.getStaffImage().getProfileUrl())
                .is_deleted(this.is_deleted())
                .build();
    }
}
