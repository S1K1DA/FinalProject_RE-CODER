package com.heartlink.member.controller;

import com.heartlink.member.utill.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendVerificationEmail(@RequestParam String email) {
        System.out.println(email);
        try {
            emailService.sendVerificationEmail(email);
            return "인증 코드가 이메일로 전송되었습니다.";
        } catch (MessagingException e) {
            return "이메일 전송에 실패했습니다.";
        }
    }

    @PostMapping("/verify")
    public String verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = emailService.verifyCode(email, code);
        return isVerified ? "인증 성공!" : "인증 실패, 잘못된 코드입니다.";
    }
}
