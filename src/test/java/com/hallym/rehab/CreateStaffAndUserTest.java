package com.hallym.rehab;

import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.repository.PatientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.Collections;

@SpringBootTest
public class CreateStaffAndUserTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    StaffRepository staffRepository;
    @Autowired
    PatientRepository patientRepository;

    Staff doctor, therapist;
    Patient patient;

    @Test
    @Rollback(value = false)
    @DisplayName("편의성 테스트 - 의사, 재활치료사 생성")
    void setUp() {
        doctor = Staff.builder()
                .mid("doctor1")
                .name("이동헌")
                .password( passwordEncoder.encode("1111") )
                .hospital("강원대학교병원")
                .department("재활의학과")
                .email("tyawebnr@hallym.com")
                .phone("01052112154")
                .roleSet(Collections.singleton(MemberRole.DOCTOR))
                .build();

        therapist = Staff.builder()
                .mid("therapist1")
                .name("홍길동")
                .password( passwordEncoder.encode("1111") )
                .hospital("강원대학교병원")
                .department("정형외과")
                .email("bestpapa@hallym.com")
                .phone("01052112154")
                .roleSet(Collections.singleton(MemberRole.THERAPIST))
                .build();

        patient = Patient.builder()
                .mid("jyp")
                .name("박주영")
                .password( passwordEncoder.encode("1111") )
                .birth(LocalDate.of(2000, 12, 13))
                .sex("Male")
                .phone("01090594356")
                .build();

        staffRepository.save(doctor);
        staffRepository.save(therapist);
        patientRepository.save(patient);
    }
}
