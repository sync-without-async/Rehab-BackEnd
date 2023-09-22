package com.hallym.rehab.domain.room.controller;

import com.hallym.rehab.domain.room.dto.RoomResponseDTO;
import com.hallym.rehab.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/room/{rno}")
    public RoomResponseDTO getRoom(@PathVariable("rno") UUID rno) {
        return roomService.getRoom(rno);
    }

}
