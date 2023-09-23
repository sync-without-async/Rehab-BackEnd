package com.hallym.rehab.domain.room.service;

import com.hallym.rehab.domain.room.dto.RoomResponseDTO;
import com.hallym.rehab.domain.room.entity.Room;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface RoomService {
    Room registerRoom(String admin_id, String user_id);
    RoomResponseDTO getRoom(UUID rno);
}
