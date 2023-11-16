package com.hallym.rehab.domain.program.repository;

import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.ProgramDetail;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.repository.PatientRepository;
import com.hallym.rehab.domain.video.entity.Tag;
import com.hallym.rehab.domain.video.entity.Video;
import com.hallym.rehab.domain.video.entity.VideoMetrics;
import com.hallym.rehab.domain.video.repository.VideoMetricsRepository;
import com.hallym.rehab.domain.video.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.Collections;

@SpringBootTest
class ProgramRepositoryTestWithDetail {

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
                .roleSet(Collections.singleton(MemberRole.DOCTOR))
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

        video = Video.builder()
                .staff(staff)
                .title("동작 제목")
                .description("동작 설명")
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

    @Test
    @Rollback(value = false)
    void createProgramAndDetailAndMetrics() {
        Program program = Program.builder()
                .staff(staff)
                .patient(patient)
                .description("몸이 안좋아요..")
                .build();

        programRepository.save(program);

        ProgramDetail programDetail = ProgramDetail.builder()
                .program(program)
                .video(video)
                .ord(1)
                .build();

        programDetailRepository.save(programDetail);

        VideoMetrics videoMetrics = VideoMetrics.builder()
                .patient(patient)
                .programDetail(programDetail)
                .metrics(80.3)
                .build();

        videoMetricsRepository.save(videoMetrics);
    }
}