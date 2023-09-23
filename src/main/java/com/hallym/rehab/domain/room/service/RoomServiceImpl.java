package com.hallym.rehab.domain.room.service;

import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.admin.repository.AdminRepository;
import com.hallym.rehab.domain.room.entity.Room;
import com.hallym.rehab.domain.room.dto.RoomResponseDTO;
import com.hallym.rehab.domain.room.repository.RoomRepository;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;


    @Override
    public Room registerRoom(String admin_id, String user_id) {
        Admin admin = adminRepository.findById(admin_id)
                .orElseThrow(() -> new RuntimeException("해당 아이디는 없는 관리자입니다."));
        Member user = memberRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("해당 아이디는 없는 사용자입니다."));


        return roomRepository.save(Room.builder()
                .admin(admin)
                .user(user)
                .build());
    }

    @Override
    public RoomResponseDTO getRoom(UUID uuid) {
        Optional<Room> byId = roomRepository.findById(uuid);
        if (byId.isEmpty()) throw new IllegalArgumentException("존재하지 않는 방입니다.");
        return byId.get().toRoomResponseDTO();
    }
}
