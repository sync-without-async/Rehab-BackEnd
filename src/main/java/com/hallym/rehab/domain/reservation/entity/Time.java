package com.hallym.rehab.domain.reservation.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hallym.rehab.domain.user.entity.Staff;
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
@Table(name = "reservation_time")
@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    private LocalDate date;

    @Column(name = "time_index")
    private int index;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid", nullable = false)
    private Staff staff;

    public void setStaff(Staff staff) {
        // 기존 관계 제거
        if (this.staff != null) {
            this.staff.getTimeList().remove(this);
        }
        this.staff = staff;
        staff.getTimeList().add(this);
    }
}
