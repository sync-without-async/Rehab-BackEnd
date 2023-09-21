package com.hallym.rehab.domain.room.service;

import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.admin.repository.AdminRepository;
import com.hallym.rehab.domain.room.domain.Room;
import com.hallym.rehab.domain.room.dto.AudioRequestDTO;
import com.hallym.rehab.domain.room.repository.AudioRepository;
import com.hallym.rehab.domain.room.repository.RoomRepository;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
//@Transactional
class AudioServiceImplTest {
    @Autowired
    RoomService roomService;
    @Autowired
    AudioService audioService;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AudioRepository audioRepository;


    Admin admin;
    Member user;

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

        user = Member.builder()
                .mid("jyp")
                .name("박주영")
                .password("1111")
                .age(22)
                .sex("Male")
                .phone("01090594356")
                .roleSet(Collections.singleton(MemberRole.USER))
                .build();

        adminRepository.save(admin);
        memberRepository.save(user);
        memberRepository.flush();

        roomService.registerRoom(admin.getMid(), user.getMid());
        roomRepository.flush();
    }

    @Test
//    @Rollback(value = false)
    @DisplayName("Audio 유저, 어드민 생성")
    void createAudio() throws IOException {
        String audioFilePath = "src/main/resources/sample.mp4";
        byte[] mp4Bytes = Files.readAllBytes(Paths.get(audioFilePath));
        // MockMultipartFile로 변환
        MultipartFile audioFile = new MockMultipartFile(
                "file",           // 필드 이름
                "sample.mp4",      // 원본 파일 이름
                "video/mp4",      // 파일 타입
                mp4Bytes           // 바이트 배열로 읽은 MP4 파일 데이터
        );

        Room room = roomRepository.findByAdminAndUser(admin.getMid(), user.getMid())
                .orElseThrow(() -> new RuntimeException("유저와 관리자에 의해 매칭된 방이 없습니다."));

        AudioRequestDTO user = AudioRequestDTO.builder()
                .audioFile(audioFile)
                .rno(room.getRno())
                .is_user(true).build();

        String result = audioService.registerAudio(user);
        assertThat(result).isEqualTo("Success create Audio");

        AudioRequestDTO admin = AudioRequestDTO.builder()
                .audioFile(audioFile)
                .rno(room.getRno())
                .is_user(false).build();

        audioService.registerAudio(admin);
        assertThat(result).isEqualTo("Success create Audio");
    }

    @Test
    void deleteAllRoomAndAudio() {
        audioService.deleteAllRoomAndAudio();
        assertThat(audioRepository.findAll().size()).isEqualTo(0);
        assertThat(roomRepository.findAll().size()).isEqualTo(0);
    }
}