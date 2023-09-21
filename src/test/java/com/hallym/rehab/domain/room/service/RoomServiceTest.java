package com.hallym.rehab.domain.room.service;

import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.admin.repository.AdminRepository;
import com.hallym.rehab.domain.room.entity.Room;
import com.hallym.rehab.domain.room.dto.RoomResponseDTO;
import com.hallym.rehab.domain.room.repository.RoomRepository;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class RoomServiceTest {
    @Autowired
    RoomService roomService;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoomRepository roomRepository;

    Admin admin;
    Member user;


    @BeforeEach
    @DisplayName("유저 생성")
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
    }

    @Test
    @DisplayName("룸 생성 테스트")
    void registerRoom() {
        String result = roomService.registerRoom(admin.getMid(), user.getMid());
        assertThat(result).isEqualTo("success registerRoom");
    }

    @Test
    @DisplayName("룸 생성 실패 테스트 - 이미 있는 방")
    void registerRoom_Fail() {
        String result = roomService.registerRoom(admin.getMid(), user.getMid());
        assertThat(result).isEqualTo("success registerRoom");

        String result2 = roomService.registerRoom(admin.getMid(), user.getMid());
        assertThat(result2).isEqualTo("already exist room");
    }

    @Test
    @DisplayName("룸 삭제 테스트")
    void deleteRoom() {
        //given
        roomService.registerRoom(admin.getMid(), user.getMid());
        //when
        Optional<Room> byAdminAndUser = roomRepository.findByAdminAndUser(admin.getMid(), user.getMid());
        Room room = byAdminAndUser.get();
        roomService.deleteRoom(room.getRno());
        //then
        assertThat(room.is_deleted()).isTrue();
    }

    @Test
    @DisplayName("룸 복원 테스트")
    void revertRoom() {
        //given
        roomService.registerRoom(admin.getMid(), user.getMid());
        //when
        Optional<Room> byAdminAndUser = roomRepository.findByAdminAndUser(admin.getMid(), user.getMid());
        Room room = byAdminAndUser.get();
        roomService.deleteRoom(room.getRno());
        roomRepository.flush();
        roomService.revertDeleteRoom(room.getRno());
        //then
        assertThat(room.is_deleted()).isFalse();
    }

    @Test
    @DisplayName("룸 리스트 조회 - 어드민")
    void getRoomListByAdmin() {
        //given
        roomService.registerRoom(admin.getMid(), user.getMid());
        //when
        List<RoomResponseDTO> roomListByAdmin = roomService.getRoomListByAdmin(admin.getMid());
        //then
        assertThat(roomListByAdmin.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("룸 리스트 조회 - 유저")
    void getRoomListByUser() {
        //given
        roomService.registerRoom(admin.getMid(), user.getMid());
        //when
        List<RoomResponseDTO> roomListByUser = roomService.getRoomListByUser(user.getMid());
        //then
        assertThat(roomListByUser.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("룸 단건 조회")
    void getRoom() {
        //given
        roomService.registerRoom(admin.getMid(), user.getMid());
        //when
        Optional<Room> byAdminAndUser = roomRepository.findByAdminAndUser(admin.getMid(), user.getMid());
        RoomResponseDTO roomResponseDTO = roomService.getRoom(byAdminAndUser.get().getRno());
        //then
        assertThat(roomResponseDTO.getAdmin_id()).isEqualTo(admin.getMid());
        assertThat(roomResponseDTO.getUser_id()).isEqualTo(user.getMid());
    }
}