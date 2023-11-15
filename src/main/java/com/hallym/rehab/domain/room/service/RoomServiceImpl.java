package com.hallym.rehab.domain.room.service;

import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.domain.room.entity.Room;
import com.hallym.rehab.domain.room.dto.RoomResponseDTO;
import com.hallym.rehab.domain.room.repository.RoomRepository;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final StaffRepository staffRepository;
    private final PatientRepository patientRepository;


    @Override
    public Room registerRoom(String staff_id, String patient_id) {
        Staff staff = staffRepository.findById(staff_id)
                .orElseThrow(() -> new RuntimeException("해당 아이디는 없는 관리자입니다."));
        Patient patient = patientRepository.findById(patient_id)
                .orElseThrow(() -> new RuntimeException("해당 아이디는 없는 사용자입니다."));


        return roomRepository.save(Room.builder()
                .staff(staff)
                .patient(patient)
                .build());
    }

    @Override
    public RoomResponseDTO getRoom(UUID uuid) {
        Optional<Room> byId = roomRepository.findById(uuid);
        if (byId.isEmpty()) throw new IllegalArgumentException("존재하지 않는 방입니다.");
        return byId.get().toRoomResponseDTO();
    }
}
