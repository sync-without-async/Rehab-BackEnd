package com.hallym.rehab.domain.program.repository;

import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.admin.repository.AdminRepository;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.ProgramDetail;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.repository.MemberRepository;
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

import java.util.Collections;

@SpringBootTest
class ProgramRepositoryTestWithDetail {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    VideoMetricsRepository videoMetricsRepository;
    @Autowired
    ProgramRepository programRepository;
    @Autowired
    ProgramDetailRepository programDetailRepository;

    Admin admin;
    Member member;
    Video video;

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

        member = Member.builder()
                .mid("jyp")
                .name("박주영")
                .password("1111")
                .age(26)
                .sex("Male")
                .phone("01052112154")
                .roleSet(Collections.singleton(MemberRole.USER))
                .build();

        adminRepository.save(admin);
        memberRepository.save(member);

        video = Video.builder()
                .admin(admin)
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
                .admin(admin)
                .user(member)
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
                .user(member)
                .programDetail(programDetail)
                .metrics(80.3)
                .build();

        videoMetricsRepository.save(videoMetrics);
    }
}