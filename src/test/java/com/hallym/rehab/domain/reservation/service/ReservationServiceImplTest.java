package com.hallym.rehab.domain.reservation.service;

import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.admin.repository.AdminRepository;
import com.hallym.rehab.domain.reservation.dto.ReservationRequestDTO;
import com.hallym.rehab.domain.reservation.entity.Time;
import com.hallym.rehab.domain.reservation.repository.TimeRepository;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReservationServiceImplTest {

    @Autowired
    ReservationService reservationService;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TimeRepository timeRepository;
    Admin admin;
    Member user;

    @BeforeEach
    void setUp() {
        admin = Admin.builder()
                .mid("ldh")
                .name("이동헌")
                .password("1111")
                .age(26)
                .sex("Male")
                .phone("01052112154")
                .roleSet(Collections.singleton(MemberRole.ADMIN))
                .build();

        user = Member.builder()
                .mid("jyp")
                .name("박주영")
                .password("1111")
                .age(22)
                .sex("Male")
                .phone("01090594356")
                .roleSet(Collections.singleton(MemberRole.USER))
                .build();

        adminRepository.save(admin);
        memberRepository.save(user);
    }

    @Test
    @Rollback(value = false)
    void createReservation() {
        ReservationRequestDTO reservationRequestDTO = ReservationRequestDTO.builder()
                .admin_id(admin.getMid())
                .user_id(user.getMid())
                .content("개발하다가 마음이 아파졌어요..")
                .date(LocalDate.of(2023, 9, 20))
                .index(3)
                .build();

        String result = reservationService.createReservation(reservationRequestDTO);
        assertThat(result).isEqualTo("success");

        reservationRequestDTO.setIndex(4);
        String result2 = reservationService.createReservation(reservationRequestDTO);
        assertThat(result2).isEqualTo("success");

        List<Time> timeList = timeRepository.findByAdmin(admin.getMid());
        assertThat(timeList.size()).isEqualTo(2);
    }
}