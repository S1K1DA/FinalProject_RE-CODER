package com.heartlink.member.utill;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtill {

    private final JavaMailSender javaMailSender;

    public EmailUtill(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setText(text, true); // true를 설정하면 HTML 형식으로 보낼 수 있습니다.
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("bamtol2100@naver.com"); // 발신자 이메일 설정

        javaMailSender.send(mimeMessage);
    }
}
