package com.hallym.rehab.domain.user.controller;

import com.hallym.rehab.domain.user.dto.*;
import com.hallym.rehab.domain.user.service.APIUserService;
import com.hallym.rehab.global.exception.IncorrectPasswordException;
import com.hallym.rehab.global.exception.MidExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
public class UserController {

    private final APIUserService apiUserService;

    @PostMapping("/join")
    public ResponseEntity<String> joinNewUser(@Valid @RequestBody StaffRequestDTO staffRequestDTO) throws MidExistsException {
        apiUserService.registerUser(staffRequestDTO);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PreAuthorize("authentication.principal.username == #mid")
    @GetMapping("/auth/staff/info/{mid}")
    public StaffResponseDTO getStaffInfo(@PathVariable String mid) {

        return apiUserService.getStaffInfo(mid);
    }

    @PreAuthorize("authentication.principal.username == #mid")
    @GetMapping("/auth/patient/info/{mid}")
    public PatientDTO getPatientInfo(@PathVariable String mid) {

        return apiUserService.getPatientInfo(mid);
    }

    @PreAuthorize("authentication.principal.username == #passwordChangeDTO.mid")
    @PostMapping("/auth/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) throws IncorrectPasswordException {

        String username = passwordChangeDTO.getMid();
        apiUserService.changePassword(username, passwordChangeDTO);

        return ResponseEntity.ok("비밀번호 변경 완료");
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @GetMapping("/auth/getTherapistList")
    public List<TherapistDTO> getTherapistList() {

        return apiUserService.getTherapistList();
    }

    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    @GetMapping("/auth/myStaff/{patient_mid}")
    public MyStaffDetailDTO getMyStaffInfo(@PathVariable String patient_mid) {

        return apiUserService.getMyStaffInformation(patient_mid);
    }

}
