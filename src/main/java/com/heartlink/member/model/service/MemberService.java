package com.heartlink.member.model.service;

import com.heartlink.member.model.dto.MemberDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final BCryptPasswordEncoder passwordEncoder;

    public MemberService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void registerMember(MemberDto memberDto) {
        String encryptedPassword = passwordEncoder.encode(memberDto.getPassword());
        System.out.println("암호화된 비밀번호: " + encryptedPassword);
    }
}
