package com.heartlink.member.model.service;

import com.heartlink.member.model.dto.MemberDto;
import com.heartlink.member.model.dto.AdminDto;
import com.heartlink.member.model.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Autowired
    public CustomUserDetailsService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. 일반 사용자 찾기
        MemberDto member = memberMapper.findByEmail(email);
        if (member != null) {
            return new User(member.getEmail(), member.getPassword(), Collections.emptyList());
        }

        // 2. 어드민 사용자 찾기
        AdminDto admin = memberMapper.findAdminByEmail(email);
        if (admin != null) {
            return new User(admin.getEmail(), admin.getPassword(), Collections.emptyList());
        }

        // 3. 사용자 또는 어드민이 없으면 예외 발생
        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email);
    }
}
