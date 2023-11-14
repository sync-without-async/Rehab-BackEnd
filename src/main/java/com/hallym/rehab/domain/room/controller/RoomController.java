package com.hallym.rehab.domain.room.controller;

import com.hallym.rehab.domain.room.dto.AudioRequestDTO;
import com.hallym.rehab.domain.room.dto.RoomResponseDTO;
import com.hallym.rehab.domain.room.service.AudioService;
import com.hallym.rehab.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final AudioService audioService;

    @GetMapping("/room/{rno}")
    public RoomResponseDTO getRoom(@PathVariable("rno") UUID rno) {
        return roomService.getRoom(rno);
    }

    @PostMapping("/audio")
    public ResponseEntity<String> createAudio(AudioRequestDTO requestDTO) {
        String result = audioService.registerAudio(requestDTO);
        return ResponseEntity.ok(result);
    }
}
