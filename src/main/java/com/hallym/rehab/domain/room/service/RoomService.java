package com.hallym.rehab.domain.room.service;

import com.hallym.rehab.domain.room.dto.RoomResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface RoomService {
    String registerRoom(String admin_id, String user_id);
    String deleteRoom(UUID rno);
    String revertDeleteRoom(UUID rno);
    List<RoomResponseDTO> getRoomListByAdmin(String admin_id);
    List<RoomResponseDTO> getRoomListByUser(String user_id);
    RoomResponseDTO getRoom(UUID rno);
}
