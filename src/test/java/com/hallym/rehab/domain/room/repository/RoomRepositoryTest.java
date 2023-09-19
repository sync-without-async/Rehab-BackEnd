package com.hallym.rehab.domain.room.repository;

import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.admin.repository.AdminRepository;
import com.hallym.rehab.domain.room.domain.Room;
import com.hallym.rehab.domain.room.dto.RoomResponseDTO;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
class RoomRepositoryTest {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    MemberRepository memberRepository;

    Admin admin;
    Admin admin2;
    Member user;
    Member user2;

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

        admin2 = Admin.builder()
                .mid("ldh2")
                .name("이동헌2")
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

        user2 = Member.builder()
                .mid("jyp2")
                .name("박주영2")
                .password("1111")
                .age(22)
                .sex("Male")
                .phone("01090594356")
                .roleSet(Collections.singleton(MemberRole.USER))
                .build();

        adminRepository.save(admin);
        adminRepository.save(admin2);
        memberRepository.save(user);
        memberRepository.save(user2);
        adminRepository.flush();
        memberRepository.flush();
    }

    @Test
    @DisplayName("룸 생성 테스트")
    void registerRoom() {
        Room room = Room.builder()
                .admin(admin)
                .user(user)
                .build();
        roomRepository.save(room);
        Optional<Room> byAdminAndUser = roomRepository.findByAdminAndUser(admin.getMid(), user.getMid());
        assertThat(byAdminAndUser.isPresent()).isTrue();
    }

    @Test
    @DisplayName("룸 삭제 테스트")
    void deleteRoom() {
        Room room = Room.builder()
                .admin(admin)
                .user(user)
                .build();
        roomRepository.save(room);
        roomRepository.flush();

        Optional<Room> byAdminAndUser = roomRepository.findByAdminAndUser(admin.getMid(), user.getMid());
        assertThat(byAdminAndUser.isPresent()).isTrue();

        Room getRoom = byAdminAndUser.get();
        getRoom.delete();
        roomRepository.save(getRoom);

        assertThat(getRoom.is_deleted()).isTrue();
    }

    @Test
    @DisplayName("룸 리스트 조회 - 어드민")
    void getListByAdmin() {
        Room room = Room.builder()
                .admin(admin)
                .user(user)
                .build();
        roomRepository.save(room);
        roomRepository.flush();

        Room room2 = Room.builder()
                .admin(admin)
                .user(user2)
                .build();
        roomRepository.save(room2);
        roomRepository.flush();

        List<RoomResponseDTO> byRoomListByAdmin = roomRepository.findByRoomListByAdmin(admin.getMid());
        assertThat(byRoomListByAdmin.size()).isEqualTo(2);
    }
}