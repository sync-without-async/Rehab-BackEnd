package com.hallym.rehab.domain.program.service;

import com.hallym.rehab.domain.program.dto.video.VideoRequestDTO;
import com.hallym.rehab.domain.program.dto.video.VideoResponseDTO;
import com.hallym.rehab.domain.program.entity.Category;
import com.hallym.rehab.domain.program.entity.Position;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.repository.ProgramRepository;
import com.hallym.rehab.domain.program.repository.VideoRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Log4j2
@Transactional
class VideoServiceImplTest {

    @Autowired
    VideoService videoService;
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    ProgramService programService;
    @Autowired
    ProgramRepository programRepository;

    Long setUp() throws IOException {
        // 프로그램 등록
        Program program = Program.builder()
                .programTitle("sample title")
                .description("Program test")
                .category(Category.SHOULDERS)
                .position(Position.LYING)
                .build();

        Program savedProgram = programRepository.save(program);

        // 프로그램 1번에 Video 등록
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
                "application/json",      // 파일 타입
                jsonBytes           // 바이트 배열로 읽은 MP4 파일 데이터
        );

        log.info("-----------------------------");
        log.info(mp4File.getSize());
        log.info(jsonFile.getSize());

        MultipartFile[] files = new MultipartFile[2];
        files[0] = mp4File;
        files[1] = jsonFile;

        VideoRequestDTO videoRequestDTO = VideoRequestDTO.builder()
                .actName("act 1")
                .playTime(13.2)
                .frame(150L)
                .files(files)
                .build();

        Long pno = savedProgram.getPno();
        Long ord = 1L;
        String result = videoService.createVideo(pno, ord, videoRequestDTO);
        log.info(result);

        return pno;
    }

    @Test
    public void getVideoList() throws IOException {
        //given
        Long pno = setUp();
        log.info("getVideoList pno = " + pno);
        //when
        VideoResponseDTO videoList = videoService.getVideoList(pno);
        //then
        assertThat(videoList.getVno_videoUrl().size()).isEqualTo(1);
    }

    @Test
    public void deleteVideo () throws IOException {
        //given
        Long pno = setUp();
        log.info("deleteVideo pno = " + pno);
        Long ord = 1L;

        Optional<Program> byId = programRepository.findById(pno);
        Program program = byId.get();
        //when
        String result = videoService.deleteVideo(pno, ord);
        log.info(result);
        //then
        assertThat(program.getVideo().size()).isEqualTo(0);
    }
}