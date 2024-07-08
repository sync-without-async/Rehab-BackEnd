package com.hallym.rehab.domain.user.service;

import com.hallym.rehab.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class SmtpGmailSenderImpl implements SmtpGmailSenderService{
    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendEmail(String toEmail) {
        if(redisUtil.existData(toEmail)) {
            redisUtil.deleteData(toEmail);
        }

        String cipher = createCipher();
        redisUtil.setDataExpire(toEmail, cipher, 60 * 5L);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toEmail);
        message.setSubject("이메일 인증번호를 알려드립니다.");
        message.setText("5분안에 입력해야 합니다. \n" + "인증번호 : " + cipher);

        emailSender.send(message);
    }

    @Override
    public Boolean verifyAuthCode(String email, String authCode) {
        String authCodeFoundByEmail = redisUtil.getData(email);
        if (authCodeFoundByEmail == null) {
            return false;
        }
        return authCodeFoundByEmail.equals(authCode);
    }

    @Override
    public String createCipher() {
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'ß
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <=57 || i >=65) && (i <= 90 || i>= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
