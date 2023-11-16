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
    @GetMapping("/auth/user/info/{mid}")
    public ResponseEntity<StaffResponseDTO> getStaffInfo(@PathVariable String mid) {
        String securityContextHolder = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info("----getname-" + securityContextHolder);

        StaffResponseDTO staffResponseDTO = apiUserService.getStaffInfo(mid);
        return ResponseEntity.ok(staffResponseDTO);
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

}
