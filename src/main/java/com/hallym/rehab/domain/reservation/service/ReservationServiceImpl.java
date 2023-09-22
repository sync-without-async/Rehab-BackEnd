package com.hallym.rehab.domain.reservation.service;

import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.admin.repository.AdminRepository;
import com.hallym.rehab.domain.reservation.dto.ReservationRequestDTO;
import com.hallym.rehab.domain.reservation.entity.Reservation;
import com.hallym.rehab.domain.reservation.entity.Time;
import com.hallym.rehab.domain.reservation.repository.ReservationRepository;
import com.hallym.rehab.domain.reservation.repository.TimeRepository;
import com.hallym.rehab.domain.room.entity.Room;
import com.hallym.rehab.domain.room.service.RoomService;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{

    private final RoomService roomService;

    private final TimeRepository timeRepository;
    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;


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
        admin.addTime(time);
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
}