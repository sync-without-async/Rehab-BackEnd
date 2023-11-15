package com.hallym.rehab.domain.user.service;

import com.hallym.rehab.domain.user.dto.PasswordChangeDTO;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.dto.MemberJoinDTO;
import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.repository.MemberRepository;

import com.hallym.rehab.global.exception.IncorrectPasswordException;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ToString
@Service
@Log4j2
public class APIUserServiceImpl implements APIUserService{

    private final PatientRepository patientRepository;
    private final StaffRepository staffRepository;

    private final PasswordEncoder passwordEncoder;

    public String getRoleSetByMid(String mid) {
        Staff staff = staffRepository.findById(mid)
                .orElseThrow(() -> new UsernameNotFoundException("해당 아이디는 없는 사용자입니다."));
        String role = String.join(",", staff.getRoleSet().stream().map(StaffRole::getValue).collect(Collectors.toList()));

        log.info("해당 유저는 " + role + " 권한을 가지고 있습니다.");

        if (staff != null) {
            return role;
        } else {
            return "member 정보를 제대로 가져오지 못했습니다";
        }
    }

    public void changePassword(String mid, PasswordChangeDTO passwordChangeDTO){
        Staff staff = staffRepository.findById(mid)
                .orElseThrow(() -> new RuntimeException("해당 아이디는 없는 사용자입니다."));

        if (!passwordEncoder.matches(passwordChangeDTO.getCurrentPassword(), staff.getPassword())) {
            throw new IncorrectPasswordException();
        }

        staff.changePassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
        staffRepository.updatePassword(mid, staff.getPassword());
    }

    public void registerUser(StaffRequestDTO staffRequestDTO){

        String mid = staffRequestDTO.getMid();

        boolean exists = staffRepository.existsById(mid);

        if(exists) {
            throw new MidExistsException();
        }

        Staff staff = Staff.builder()
                .mid(staffRequestDTO.getMid())
                .name(staffRequestDTO.getName())
                .password(staffRequestDTO.getPassword())
                .hospital(staffRequestDTO.getHospital())
                .department(staffRequestDTO.getDepartment())
                .email(staffRequestDTO.getEmail())
                .phone(staffRequestDTO.getPhone())
                .build();

        if(staffRequestDTO.getFileName() != null){
            String[] arr = staffRequestDTO.getFileName().split("_");
            staff.addImage(arr[0], arr[1]);
        }

        staff.changePassword(passwordEncoder.encode(staffRequestDTO.getPassword()));

        if(staffRequestDTO.getStaffRole().equals(StaffRole.DOCTOR.getValue())){
            staff.addRole(StaffRole.DOCTOR);
        }
        else if(staffRequestDTO.getStaffRole().equals(StaffRole.THERAPIST.getValue())){
            staff.addRole(StaffRole.THERAPIST);
        }

        staffRepository.save(staff);
    }

}
