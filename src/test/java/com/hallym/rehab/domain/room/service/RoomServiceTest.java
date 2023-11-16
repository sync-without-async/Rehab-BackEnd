package com.hallym.rehab.domain.room.service;

import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.domain.room.entity.Room;
import com.hallym.rehab.domain.room.dto.RoomResponseDTO;
import com.hallym.rehab.domain.room.repository.RoomRepository;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class RoomServiceTest {
    @Autowired
    RoomService roomService;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    RoomRepository roomRepository;

    Staff staff;
    Patient patient;


    @BeforeEach
    @DisplayName("유저 생성")
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
        patientRepository.flush();
    }

    @Test
    @DisplayName("룸 생성 테스트")
    void registerRoom() {
        Room room = roomService.registerRoom(staff.getMid(), patient.getMid());
    }

    @Test
    @DisplayName("룸 단건 조회")
    void getRoom() {
        //given
        roomService.registerRoom(staff.getMid(), patient.getMid());
        //when
        Optional<Room> byAdminAndUser = roomRepository.findByStaffAndPatient(staff.getMid(), patient.getMid());
        RoomResponseDTO roomResponseDTO = roomService.getRoom(byAdminAndUser.get().getRno());
        //then
        assertThat(roomResponseDTO.getStaff_id()).isEqualTo(staff.getMid());
        assertThat(roomResponseDTO.getPatient_id()).isEqualTo(patient.getMid());
    }
}