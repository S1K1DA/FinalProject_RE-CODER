package com.heartlink.member.util;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {

    private final EmailUtil emailUtil;
    private final Map<String, String> emailVerificationMap = new HashMap<>();

    public EmailService(EmailUtil emailUtil) {
        this.emailUtil = emailUtil;
    }

    // 인증 코드 생성
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000; // 100000~999999 범위의 6자리 숫자 생성
        return String.valueOf(code);
    }

    // 이메일로 인증 코드 전송
    public void sendVerificationEmail(String email) throws MessagingException {
        String code = generateVerificationCode();
        emailVerificationMap.put(email, code);

        String subject = "HeartLink 이메일 인증 코드";
        String text = "<h3>HeartLink 이메일 인증 코드</h3><p>인증 코드는 <strong>" + code + "</strong> 입니다.</p>";

        emailUtil.sendEmail(email, subject, text);
    }

    // 인증 코드 유효성 검사
    public boolean verifyCode(String email, String code) {
        String storedCode = emailVerificationMap.get(email);
        return storedCode != null && storedCode.equals(code);
    }
}
