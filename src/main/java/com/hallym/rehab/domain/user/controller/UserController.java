package com.hallym.rehab.domain.user.controller;

import com.hallym.rehab.domain.user.dto.PasswordChangeDTO;
import com.hallym.rehab.domain.user.dto.MemberJoinDTO;
import com.hallym.rehab.domain.user.service.APIUserService;
import com.hallym.rehab.global.exception.IncorrectPasswordException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final APIUserService apiUserService;

    @PostMapping("/join")
    public ResponseEntity<String> joinNewUser(@Valid @RequestBody MemberJoinDTO memberJoinDTO) {
        try {
            apiUserService.registerUser(memberJoinDTO);
            return ResponseEntity.ok("회원가입 성공");
        } catch (APIUserService.MidExistsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 존재하는 이메일입니다");
        }
    }

    @PreAuthorize("authentication.principal.username == #passwordChangeDTO.mid")
    @PostMapping("/auth/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {

        String securityContextHolder = SecurityContextHolder.getContext().getAuthentication().getName();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserDetails userDetails =(UserDetails) principal;
        log.info("----getname-" + securityContextHolder);
        log.info("---prin--" + userDetails.getUsername());

        log.info("---------" + passwordChangeDTO.getMid());
        String username = passwordChangeDTO.getMid();

        try {
            apiUserService.changePassword(username, passwordChangeDTO);
            return ResponseEntity.ok("비밀번호 변경 완료");
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("현재 비밀번호가 아닙니다");
        }
    }

}
