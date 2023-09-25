package com.hallym.rehab.domain.reservation.repository;

import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.admin.repository.AdminRepository;
import com.hallym.rehab.domain.reservation.entity.Time;
import com.hallym.rehab.domain.user.entity.MemberRole;
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
    AdminRepository adminRepository;
    @Autowired
    TimeRepository timeRepository;
    Admin admin;
    @BeforeEach
    void setUpAdmin() {
        admin = Admin.builder()
                .mid("ldh")
                .name("이동헌")
                .password("1111")
                .age(26)
                .sex("Male")
                .phone("01052112154")
                .roleSet(Collections.singleton(MemberRole.ADMIN))
                .build();

        adminRepository.save(admin);
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

        time.setAdmin(admin);
        time2.setAdmin(admin);

        timeRepository.save(time);
        timeRepository.save(time2);

        assertThat(admin.getTimeList().size()).isEqualTo(2);
        assertThat(time.getAdmin()).isEqualTo(admin);
    }
}