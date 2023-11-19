package com.hallym.rehab.domain.video.service;

import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.video.dto.VideoRequestDTO;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.domain.video.entity.Tag;
import com.hallym.rehab.domain.video.entity.Video;
import com.hallym.rehab.domain.video.repository.VideoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VideoServiceTest {
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    VideoService videoService;
    @Autowired
    StaffRepository staffRepository;

    Staff staff;

    @BeforeEach
    void setUp() {
        staff = Staff.builder()
                .mid("jyp")
                .name("박주영")
                .password("1111")
                .hospital("강원대학교병원")
                .department("재활의학과")
                .email("tyawebnr@hallym.com")
                .phone("01052112154")
                .roleSet(Collections.singleton(MemberRole.DOCTOR))
                .build();

        staffRepository.saveAndFlush(staff);
    }

    @Test
    @Order(1)
    void createVideo() throws IOException {
        String mp4FilePath = "src/main/resources/sample.mp4";
        String jsonFilePath = "src/main/resources/sample.json";
        byte[] mp4Bytes = Files.readAllBytes(Paths.get(mp4FilePath));
        byte[] jsonBytes = Files.readAllBytes(Paths.get(jsonFilePath));

        // MockMultipartFile로 변환
        MultipartFile mp4File = new MockMultipartFile(
                "file",           // 필드 이름
                "sample.mp4",      // 원본 파일 이름
                "video/mp4",      // 파일 타입
                mp4Bytes           // 바이트 배열로 읽은 MP4 파일 데이터
        );

        MultipartFile jsonFile = new MockMultipartFile(
                "file",           // 필드 이름
                "sample.json",      // 원본 파일 이름
                "file",
                jsonBytes
        );

        MultipartFile[] files = new MultipartFile[2];
        files[0] = mp4File;
        files[1] = jsonFile;

        VideoRequestDTO videoRequestDTO = VideoRequestDTO.builder()
                .staff_id(staff.getMid())
                .title("테스트 title")
                .description("테스트 description")
                .tag(Tag.SHOULDER)
                .frame(300L)
                .playTime(18.5)
                .files(files)
                .build();

        String result = videoService.createVideo(videoRequestDTO);
        assertThat(result).isEqualTo("Success create Video");
    }

    @Test
    @Order(2)
    void deleteVideo() {
        Video video = videoRepository.findAll().get(0);
        String result = videoService.deleteVideo(video.getVno());
        assertThat(result).isEqualTo("Success delete Video");
    }
}