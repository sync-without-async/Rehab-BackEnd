package com.hallym.rehab.domain.reservation.service;

import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.admin.repository.AdminRepository;
import com.hallym.rehab.domain.reservation.dto.ReservationRequestDTO;
import com.hallym.rehab.domain.reservation.dto.ReservationResponseByAdminDTO;
import com.hallym.rehab.domain.reservation.dto.ReservationResponseByUserDTO;
import com.hallym.rehab.domain.reservation.entity.Reservation;
import com.hallym.rehab.domain.reservation.entity.Time;
import com.hallym.rehab.domain.reservation.repository.ReservationRepository;
import com.hallym.rehab.domain.reservation.repository.TimeRepository;
import com.hallym.rehab.domain.room.entity.Room;
import com.hallym.rehab.domain.room.service.RoomService;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.repository.MemberRepository;
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
public class ReservationServiceImpl implements ReservationService{

    private final RoomService roomService;

    private final TimeRepository timeRepository;
    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;


    @Override
    public PageResponseDTO<ReservationResponseByAdminDTO> getListByAdmin(String mid, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <= 0? 0:
                        pageRequestDTO.getPage()-1,
                        pageRequestDTO.getSize(),
                        Sort.by("date", "index").ascending());

        Page<Reservation> result = reservationRepository.findByMid(mid, pageable);

        List<ReservationResponseByAdminDTO> dtoList = result.getContent()
                .stream()
                .map(Reservation::toAdminDTO)
                .collect(Collectors.toList());

        return PageResponseDTO.<ReservationResponseByAdminDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<ReservationResponseByUserDTO> getListByUser(String mid, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <= 0? 0:
                        pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("date", "index").ascending());

        Page<Reservation> result = reservationRepository.findByMid(mid, pageable);

        List<ReservationResponseByUserDTO> dtoList = result.getContent()
                .stream()
                .map(Reservation::toUserDTO)
                .collect(Collectors.toList());

        return PageResponseDTO.<ReservationResponseByUserDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public List<Time> getReservedTime(String adminId, LocalDate date) {
        adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("관리자 아이디가 일치하지 않습니다."));

        return timeRepository.findAvailableTimeOfDayByAdmin(adminId, date);
    }

    @Override
    public String createReservation(ReservationRequestDTO requestDTO) {
        String adminId = requestDTO.getAdmin_id();
        String userId = requestDTO.getUser_id();
        String content = requestDTO.getContent();
        LocalDate date = requestDTO.getDate();
        int index = requestDTO.getIndex();

        Optional<Time> timeOptional = timeRepository.findReservation(adminId, date, index);
        if (timeOptional.isPresent()) return "already reserved time";

        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("관리자 아이디가 일치하지 않습니다."));
        Member user = memberRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자 아이디가 일치하지 않습니다."));

        Room room = roomService.registerRoom(adminId, userId);

        Time time = Time.builder()
                .date(date)
                .index(index)
                .build();

        time.setAdmin(admin);
        timeRepository.saveAndFlush(time);
        adminRepository.saveAndFlush(admin);

        Reservation reservation = Reservation.builder()
                .admin(admin)
                .user(user)
                .content(content)
                .date(date)
                .index(index)
                .room(room)
                .build();

        room.setReservation(reservation);

        reservationRepository.save(reservation);
        return "success";
    }

    @Override
    public String cancleReservation(Long rvno) {
        Optional<Reservation> byId = reservationRepository.findById(rvno);
        if (byId.isEmpty()) return "wrong id";

        Reservation reservation = byId.get();
        reservation.setDelete(true);

        Admin admin = reservation.getAdmin();
        LocalDate date = reservation.getDate();
        int index = reservation.getIndex();

        Optional<Time> optionalTime = timeRepository.findReservation(admin.getMid(), date, index);
        if (optionalTime.isEmpty()) return "exists rvno, but time doesn't exist";

        Time time = optionalTime.get();
        admin.getTimeList().remove(time);
        timeRepository.delete(time); // 어드민에게 예약 추가된 시간 삭제

        return "success";
    }
}