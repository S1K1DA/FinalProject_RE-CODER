package com.heartlink.member.model.service;

import com.heartlink.member.model.dto.MemberDto;
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

    // 인증 및 권한
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MemberDto member = memberMapper.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email);
        }

        // UserDetails 객체 생성하여 반환
        // 권한 정보가 필요하다면, 두 번째 파라미터에 권한 리스트를 추가할 수 있습니다.
        return new User(member.getEmail(), member.getPassword(), Collections.emptyList());
    }
}
