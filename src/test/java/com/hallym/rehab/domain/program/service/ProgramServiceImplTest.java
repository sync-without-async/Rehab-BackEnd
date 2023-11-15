package com.hallym.rehab.domain.program.service;

import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.domain.program.dto.MetricsUpdateRequestDTO;
import com.hallym.rehab.domain.program.dto.ProgramRequestDTO;
import com.hallym.rehab.domain.program.repository.ProgramDetailRepository;
import com.hallym.rehab.domain.program.repository.ProgramRepository;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.entity.StaffRole;
import com.hallym.rehab.domain.user.repository.PatientRepository;
import com.hallym.rehab.domain.video.entity.Tag;
import com.hallym.rehab.domain.video.entity.Video;
import com.hallym.rehab.domain.video.repository.VideoMetricsRepository;
import com.hallym.rehab.domain.video.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.*;

@SpringBootTest
@Slf4j
class ProgramServiceImplTest {

    @Autowired
    StaffRepository staffRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    VideoMetricsRepository videoMetricsRepository;
    @Autowired
    ProgramRepository programRepository;
    @Autowired
    ProgramDetailRepository programDetailRepository;
    @Autowired
    ProgramService programService;

    Staff staff;
    Patient patient;
    Video video;

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
                .roleSet(Collections.singleton(StaffRole.DOCTOR))
                .build();

        patient = Patient.builder()
                .mid("jyp")
                .name("박주영")
                .password("1111")
                .birth(LocalDate.of(2000, 12, 13))
                .sex("Male")
                .phone("01052112154")
                .build();

        staffRepository.save(staff);
        patientRepository.save(patient);

        for (int i = 1; i <= 10; i++) {
            video = Video.builder()
                    .staff(staff)
                    .title("동작 제목" + i)
                    .description("동작 설명" + i)
                    .tag(Tag.ARM)
                    .frame(300L)
                    .playTime(18.5)
                    .videoURL("https://...")
                    .jsonURL("https://...")
                    .videoPath("video/..")
                    .jsonPath("json/..")
                    .build();

            videoRepository.save(video);
        }
    }

    @Test
    @Rollback(value = false)
    void createProgramAndDetail() {
        Map<Integer, Long> ord_map = new HashMap<>();
        ord_map.put(1, 10L);
        ord_map.put(2, 10L);
        ord_map.put(3, 3L);
        ord_map.put(4, 3L);
        ord_map.put(5, 1L);
        ord_map.put(6, 1L);
        ord_map.put(7, 3L);
        ord_map.put(8, 6L);
        ord_map.put(9, 6L);
        ord_map.put(10, 9L);

        ProgramRequestDTO programRequestDTO = ProgramRequestDTO.builder()
                .adminId("ldh")
                .userId("jyp")
                .description("몸이 아프다구요!")
                .ord_map(ord_map)
                .build();

        String result = programService.createProgramAndDetail(programRequestDTO);
        Assertions.assertThat(result).isEqualTo("success");
    }

    @Test
    @Rollback(value = false)
    void updateMetrics() {
        MetricsUpdateRequestDTO requestDTO = MetricsUpdateRequestDTO.builder()
                .userId(patient.getMid())
                .pno(1L)
                .vno(3L)
                .ord(7)
                .metrics(83.2)
                .build();

        String savedMetricsResult = programService.updateMetrics(requestDTO);
        Assertions.assertThat(savedMetricsResult).isEqualTo("success");
    }
}