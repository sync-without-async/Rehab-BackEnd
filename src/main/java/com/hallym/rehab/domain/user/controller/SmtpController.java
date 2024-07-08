package com.hallym.rehab.domain.user.controller;

import com.hallym.rehab.domain.user.dto.EmailDTO;
import com.hallym.rehab.domain.user.service.SmtpGmailSenderImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class SmtpController {
    private final SmtpGmailSenderImpl senderService;

    @GetMapping("/send-code/{email}")
    public ResponseEntity<String> sendEmail(@PathVariable String email) {
        senderService.sendEmail(email);
        return ResponseEntity.ok("Email sent");
    }

    @PostMapping(value = "/verify-code/{email}", consumes = "application/json")
    public ResponseEntity<String> sendEmailAndCode(@PathVariable String email, @RequestBody EmailDTO dto) {
        if (senderService.verifyAuthCode(email, dto.getAuthCode())) {
            return ResponseEntity.ok("Email verified");
        }
        return ResponseEntity.badRequest().body("Email verification failed");
    }
}
