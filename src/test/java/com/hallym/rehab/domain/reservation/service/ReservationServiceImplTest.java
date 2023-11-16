package com.hallym.rehab.domain.reservation.service;

import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.domain.reservation.dto.ReservationRequestDTO;
import com.hallym.rehab.domain.reservation.entity.Reservation;
import com.hallym.rehab.domain.reservation.entity.Time;
import com.hallym.rehab.domain.reservation.repository.ReservationRepository;
import com.hallym.rehab.domain.reservation.repository.TimeRepository;
import com.hallym.rehab.domain.room.repository.AudioRepository;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.repository.PatientRepository;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
class ReservationServiceImplTest {

    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    TimeRepository timeRepository;
    @Autowired
    AudioRepository audioRepository;
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
    @Rollback(value = false)
    void createReservation() {
        ReservationRequestDTO reservationRequestDTO = ReservationRequestDTO.builder()
                .staff_id(staff.getMid())
                .patient_id(patient.getMid())
                .content("개발하다가 마음이 아파졌어요..")
                .date(LocalDate.of(2023, 9, 20))
                .index(3)
                .build();

        String result = reservationService.createReservation(reservationRequestDTO);
        assertThat(result).isEqualTo("success");

        List<Time> timeList = timeRepository.findByStaff(staff.getMid());
        assertThat(timeList.size()).isEqualTo(1);

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .build();

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <= 0? 0:
                        pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("date", "index").ascending());

        Page<Reservation> byMid = reservationRepository.findByMid(staff.getMid(), pageable);
        List<Reservation> reservationList = byMid.getContent();
        Reservation reservation = reservationList.get(0);
        assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2023, 9, 20));
        assertThat(reservation.getIndex()).isEqualTo(3);

        assertNotNull(reservation.getRoom());
    }

    @Test
    @Rollback(value = false)
    void cancleRservation() {
        ReservationRequestDTO reservationRequestDTO = ReservationRequestDTO.builder()
                .staff_id(staff.getMid())
                .patient_id(patient.getMid())
                .content("개발하다가 마음이 아파졌어요..")
                .date(LocalDate.of(2023, 9, 20))
                .index(3)
                .build();

        String result1 = reservationService.createReservation(reservationRequestDTO);
        assertThat(result1).isEqualTo("success");

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .build();

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <= 0? 0:
                        pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("date", "index").ascending());

        Page<Reservation> byMid = reservationRepository.findByMid(staff.getMid(), pageable);
        List<Reservation> reservationList = byMid.getContent();
        Reservation reservation = reservationList.get(0);

        String result2 = reservationService.cancleReservation(reservation.getRvno());
        assertThat(result2).isEqualTo("success");

        assertThat(timeRepository.findByStaff(staff.getMid()).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("create Dummy")
    @Rollback(value = false)
    void createDummy() {
        for (int i = 0; i < 120; i++) {
            ReservationRequestDTO reservationRequestDTO = ReservationRequestDTO.builder()
                    .staff_id(staff.getMid())
                    .patient_id(patient.getMid())
                    .content("개발하다가 마음이 아파졌어요.."+i)
                    .date(LocalDate.of(2023, 9, 20))
                    .index(i)
                    .build();

            reservationService.createReservation(reservationRequestDTO);
        }
    }

    @Test
    void getReservedTime() {
        //given
        ReservationRequestDTO reservationRequestDTO = ReservationRequestDTO.builder()
                .staff_id(staff.getMid())
                .patient_id(patient.getMid())
                .content("개발하다가 마음이 아파졌어요..")
                .date(LocalDate.of(2023, 9, 20))
                .index(1)
                .build();

        reservationService.createReservation(reservationRequestDTO);

        //when
        List<Time> timeList =
                reservationService.getReservedTime("ldh", LocalDate.of(2023, 9, 20));

        //then
        assertThat(timeList.size()).isEqualTo(1);
    }
}