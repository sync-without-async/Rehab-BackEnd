package com.hallym.rehab.domain.reservation.repository;

import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.admin.repository.AdminRepository;
import com.hallym.rehab.domain.reservation.entity.Reservation;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    MemberRepository memberRepository;

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
    void createReservation() {
        Reservation reservation = Reservation.builder()
                .admin(admin)
                .user(user)
                .content("개발하다가 마음이 아파졌어요..")
                .build();

        Reservation save = reservationRepository.save(reservation);
        Optional<Reservation> byId = reservationRepository.findById(save.getRvno());
        Assertions.assertThat(reservation).isEqualTo(byId.get());
    }
}