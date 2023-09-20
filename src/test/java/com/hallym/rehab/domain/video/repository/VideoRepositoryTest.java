package com.hallym.rehab.domain.video.repository;

import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.admin.repository.AdminRepository;
import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.video.entity.Tag;
import com.hallym.rehab.domain.video.entity.Video;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VideoRepositoryTest {

    @Autowired AdminRepository adminRepository;
    @Autowired VideoRepository videoRepository;

    Admin admin;

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

        adminRepository.save(admin);
    }

    @Test
//    @Rollback(value = false)
    void videoSave() {
        Video video = Video.builder()
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

        Video saveVideo = videoRepository.save(video);
        Video savedVideo = videoRepository.findById(saveVideo.getVno())
                .orElseThrow(() -> new RuntimeException("정확하지 않은 비디오 번호 입니다."));

        assertThat(saveVideo).isEqualTo(savedVideo);
    }

}