package com.hallym.rehab.domain.video.repository;

import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.domain.user.entity.StaffRole;
import com.hallym.rehab.domain.video.dto.pagedto.VideoPageRequestDTO;
import com.hallym.rehab.domain.video.dto.VideoResponseDTO;
import com.hallym.rehab.domain.video.entity.Tag;
import com.hallym.rehab.domain.video.entity.Video;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
class VideoRepositoryTest {

    @Autowired
    StaffRepository staffRepository;
    @Autowired VideoRepository videoRepository;

    Staff staff;

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

        staffRepository.save(staff);
    }

    @Test
//    @Rollback(value = false)
    void videoSave() {
        Video video = Video.builder()
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

        Video saveVideo = videoRepository.save(video);
        Video savedVideo = videoRepository.findById(saveVideo.getVno())
                .orElseThrow(() -> new RuntimeException("정확하지 않은 비디오 번호 입니다."));

        assertThat(saveVideo).isEqualTo(savedVideo);
    }

    @Test
    @Rollback(value = false)
    void videoSearch() {
        // insert dummy video data
        for (int i = 0; i < 150; i++) {
            Video video = Video.builder()
                    .staff(staff)
                    .title("동작 제목" + i)
                    .description("동작 설명" + i)
                    .tag(Tag.ARM)
                    .frame(300L + i)
                    .playTime(18.5 + i)
                    .videoURL("https://..." + i)
                    .jsonURL("https://..." + i)
                    .videoPath("video/.." + i)
                    .jsonPath("json/.." + i)
                    .build();
            videoRepository.save(video);
        }

        // search condition
        String title = "2"; // contain
        Tag tag = Tag.ARM; // equal

        VideoPageRequestDTO pageRequestDTO = VideoPageRequestDTO.builder()
                .page(1)
                .size(10)
                .title(title)
                .tag(tag)
                .build();

        Page<VideoResponseDTO> search = videoRepository.search(pageRequestDTO);
        List<VideoResponseDTO> content = search.getContent();
        log.info("total count == " + String.valueOf(content.size()));
        content.forEach(videoResponseDTO -> {
            log.info(String.valueOf(videoResponseDTO.getVno()));
            log.info(videoResponseDTO.getTitle());
            log.info(videoResponseDTO.getDescription());
            log.info(String.valueOf(videoResponseDTO.getTag()));
            log.info(String.valueOf(videoResponseDTO.getPlayTime()));
            log.info(videoResponseDTO.getVideoURL());
            log.info("========================================");
        });
    }

}