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
    public String registerRoom(String admin_id, String user_id) {
        Optional<Room> byAdminAndUser = roomRepository.findByAdminAndUser(admin_id, user_id);
        if (byAdminAndUser.isPresent()) return "already exist room";

        Admin admin = adminRepository.findById(admin_id)
                .orElseThrow(() -> new RuntimeException("해당 아이디는 없는 관리자입니다."));
        Member user = memberRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("해당 아이디는 없는 사용자입니다."));

        roomRepository.save(Room.builder()
                                .admin(admin)
                                .user(user)
                                .build());
        return "success registerRoom";
    }

    @Override
    public String deleteRoom(UUID rno) {
        Optional<Room> byId = roomRepository.findById(rno);
        if (byId.isEmpty()) return "wrong id";

        Room room = byId.get();
        room.delete();
        roomRepository.save(room);
        return "success deleteRoom";
    }

    @Override
    public String revertDeleteRoom(UUID rno) {
        Optional<Room> byId = roomRepository.findById(rno);
        if (byId.isEmpty()) return "wrong id";

        Room room = byId.get();
        room.revertDelete();
        roomRepository.save(room);
        return "success revert Room";
    }

    @Override
    public List<RoomResponseDTO> getRoomListByAdmin(String admin_id) {
        return roomRepository.findByRoomListByAdmin(admin_id);
    }

    @Override
    public List<RoomResponseDTO> getRoomListByUser(String user_id) {
        return roomRepository.findByRoomListByUser(user_id);
    }

    @Override
    public RoomResponseDTO getRoom(UUID uuid) {
        Optional<Room> byId = roomRepository.findById(uuid);
        if (byId.isEmpty()) throw new IllegalArgumentException("존재하지 않는 방입니다.");
        return byId.get().toRoomResponseDTO();
    }
}
