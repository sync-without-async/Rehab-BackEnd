package com.hallym.rehab.domain.reservation.repository;

import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.domain.reservation.entity.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class TimeRepositoryTest {

    @Autowired
    StaffRepository staffRepository;
    @Autowired
    TimeRepository timeRepository;
    Staff staff;
    @BeforeEach
    void setUpAdmin() {
        staff = Staff.builder()
                .mid("ldh")
                .name("이동헌")
                .password("1111")
                .hospital("강원대학교병원")
                .department("재활의학과")
                .email("tyawebnr@hallym.com")
                .phone("01052112154")
                .roleSet(Collections.singleton(MemberRole.DOCTOR))
                .build();

        staffRepository.save(staff);
    }

    @Test
    void createReservationTime() {
        Time time = Time.builder()
                .date(LocalDate.now())
                .index(1)
                .build();

        Time time2 = Time.builder()
                .date(LocalDate.now())
                .index(3)
                .build();

        time.setStaff(staff);
        time2.setStaff(staff);

        timeRepository.save(time);
        timeRepository.save(time2);

        assertThat(staff.getTimeList().size()).isEqualTo(2);
        assertThat(time.getStaff()).isEqualTo(staff);
    }
}