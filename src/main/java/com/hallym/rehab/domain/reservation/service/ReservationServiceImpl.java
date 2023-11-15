package com.hallym.rehab.domain.reservation.service;

import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.domain.reservation.dto.ReservationRequestDTO;
import com.hallym.rehab.domain.reservation.dto.ReservationResponseByStaffDTO;
import com.hallym.rehab.domain.reservation.dto.ReservationResponseByPatientDTO;
import com.hallym.rehab.domain.reservation.entity.Reservation;
import com.hallym.rehab.domain.reservation.entity.Time;
import com.hallym.rehab.domain.reservation.repository.ReservationRepository;
import com.hallym.rehab.domain.reservation.repository.TimeRepository;
import com.hallym.rehab.domain.room.entity.Audio;
import com.hallym.rehab.domain.room.entity.Room;
import com.hallym.rehab.domain.room.repository.AudioRepository;
import com.hallym.rehab.domain.room.service.RoomService;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.repository.PatientRepository;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final RoomService roomService;
    private final TimeRepository timeRepository;
    private final StaffRepository staffRepository;
    private final AudioRepository audioRepository;
    private final PatientRepository patientRepository;
    private final ReservationRepository reservationRepository;


    @Override
    public PageResponseDTO<ReservationResponseByStaffDTO> getListByStaff(String mid, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <= 0 ? 0 :
                        pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("date", "index").ascending());

        Page<Reservation> result = reservationRepository.findByMid(mid, pageable);

        List<ReservationResponseByStaffDTO> dtoList = result.getContent()
                .stream()
                .map(Reservation::toAdminDTO)
                .collect(Collectors.toList());

        return PageResponseDTO.<ReservationResponseByStaffDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<ReservationResponseByPatientDTO> getListByUser(String mid, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <= 0 ? 0 :
                        pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("date", "index").ascending());

        Page<Reservation> result = reservationRepository.findByMid(mid, pageable);

        List<ReservationResponseByPatientDTO> dtoList = result.getContent()
                .stream()
                .map(Reservation::toUserDTO)
                .collect(Collectors.toList());

        return PageResponseDTO.<ReservationResponseByPatientDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public List<Time> getReservedTime(String staffId, LocalDate date) {
        staffRepository.findById(staffId).orElseThrow(() -> new RuntimeException("관리자 아이디가 일치하지 않습니다."));

        return timeRepository.findAvailableTimeOfDayByStaff(staffId, date);
    }

    @Override
    public String createReservation(ReservationRequestDTO requestDTO) {
        String staffId = requestDTO.getStaff_id();
        String patientId = requestDTO.getPatient_id();
        String content = requestDTO.getContent();
        LocalDate date = requestDTO.getDate();
        int index = requestDTO.getIndex();

        Optional<Time> timeOptional = timeRepository.findReservation(staffId, date, index);
        if (timeOptional.isPresent()) {
            return "already reserved time";
        }

        Staff staff = staffRepository.findById(staffId).orElseThrow(() -> new RuntimeException("관리자 아이디가 일치하지 않습니다."));
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("사용자 아이디가 일치하지 않습니다."));

        Room room = roomService.registerRoom(staffId, patientId);

        Time time = Time.builder()
                .date(date)
                .index(index)
                .build();

        time.setStaff(staff);
        timeRepository.saveAndFlush(time);
        staffRepository.saveAndFlush(staff);

        Reservation reservation = Reservation.builder()
                .staff(staff)
                .patient(patient)
                .content(content)
                .date(date)
                .index(index)
                .room(room)
                .build();

        Audio audio = Audio.builder()
                .room(room)
                .build();

        audioRepository.save(audio);

        room.setReservation(reservation);
        room.setAudio(audio);

        reservationRepository.save(reservation);
        return "success";
    }

    @Override
    public String cancleReservation(Long rvno) {
        Optional<Reservation> byId = reservationRepository.findById(rvno);
        if (byId.isEmpty()) {
            return "wrong id";
        }

        Reservation reservation = byId.get();
        reservation.setDelete(true);

        Staff staff = reservation.getStaff();
        LocalDate date = reservation.getDate();
        int index = reservation.getIndex();

        Optional<Time> optionalTime = timeRepository.findReservation(staff.getMid(), date, index);
        if (optionalTime.isEmpty()) {
            return "exists rvno, but time doesn't exist";
        }

        Time time = optionalTime.get();
        staff.getTimeList().remove(time);
        timeRepository.delete(time); // 어드민에게 예약 추가된 시간 삭제

        return "success";
    }
}