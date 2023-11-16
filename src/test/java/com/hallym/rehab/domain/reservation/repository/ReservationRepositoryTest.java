package com.hallym.rehab.domain.reservation.repository;

import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.domain.reservation.entity.Reservation;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.repository.PatientRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@SpringBootTest
@Transactional
class ReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    PatientRepository patientRepository;

    Staff staff;
    Patient patient;

    @BeforeEach
    void setUp() {
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

        patient = Patient.builder()
                .mid("jyp")
                .name("박주영")
                .password("1111")
                .birth(LocalDate.of(2000, 12, 13))
                .sex("Male")
                .phone("01090594356")
                .build();

        staffRepository.save(staff);
        patientRepository.save(patient);
    }

    @Test
    void createReservation() {
        Reservation reservation = Reservation.builder()
                .staff(staff)
                .patient(patient)
                .content("개발하다가 마음이 아파졌어요..")
                .build();

        Reservation save = reservationRepository.save(reservation);
        Optional<Reservation> byId = reservationRepository.findById(save.getRvno());
        Assertions.assertThat(reservation).isEqualTo(byId.get());
    }
}