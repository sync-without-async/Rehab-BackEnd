package com.hallym.rehab.domain.member;


import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.repository.PatientRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collections;

@SpringBootTest
@Log4j2
public class PatientRepositoryTests {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Test
    void registerMember() {

        Patient patient = Patient.builder()
                .mid("testuser")
                .name("홍길동")
                .password("1111")
                .birth(LocalDate.of(2000, 12, 13))
                .sex("Male")
                .phone("01052112154")
                .build();

        patientRepository.save(patient);

        log.info("Register------" + patient.getName());

    }

    @Test
    void registerStaff() throws Exception {

        Staff staff = Staff.builder()
                .mid("doctor3")
                .name("홍길동")
                .password("1111")
                .hospital("서울아산병원")
                .department("재활의학과")
                .email("tyanior@naver.com")
                .phone("01052112154")
                .roleSet(Collections.singleton(MemberRole.THERAPIST))
                .build();

        staffRepository.save(staff);

        log.info("Register------" + staff.getName());

    }
}
