package com.hallym.rehab.domain.room.controller;

import com.hallym.rehab.domain.room.dto.RoomRequestDTO;
import com.hallym.rehab.domain.room.dto.RoomResponseDTO;
import com.hallym.rehab.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/room")
    public ResponseEntity<String> registerRoom(@RequestBody RoomRequestDTO roomRequestDTO) {
        String result = roomService.registerRoom(roomRequestDTO.getAdmin_id(), roomRequestDTO.getUser_id());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/rooms/user/{userId}")
    public List<RoomResponseDTO> getRoomListByUser(@PathVariable String userId) {
        return roomService.getRoomListByUser(userId);
    }

    @GetMapping("/rooms/admin/{adminId}")
    public List<RoomResponseDTO> getRoomListByAdmin(@PathVariable String adminId) {
        return roomService.getRoomListByAdmin(adminId);
    }

    @GetMapping("/room/{rno}")
    public RoomResponseDTO getRoom(@PathVariable("rno") UUID rno) {
        return roomService.getRoom(rno);
    }
}
