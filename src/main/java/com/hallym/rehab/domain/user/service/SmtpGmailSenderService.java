package com.hallym.rehab.domain.user.service;

public interface SmtpGmailSenderService {
    void sendEmail(String toEmail);
    Boolean verifyAuthCode(String email, String authCode);
    String createCipher();
}
