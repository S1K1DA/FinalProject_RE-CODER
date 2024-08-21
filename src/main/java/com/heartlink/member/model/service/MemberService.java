package com.heartlink.member.model.service;

import com.heartlink.member.model.dto.MemberDto;
import com.heartlink.member.model.mapper.MemberMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Service
public class MemberService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;

    public MemberService(BCryptPasswordEncoder passwordEncoder, MemberMapper memberMapper) {
        this.passwordEncoder = passwordEncoder;
        this.memberMapper = memberMapper;
    }

    // 이메일로 사용자 정보 조회
    public MemberDto findByEmail(String email) {
        return memberMapper.findByEmail(email);
    }

    public void registerMember(MemberDto memberDto) {
        // 이메일 중복 체크
        if (isEmailDuplicate(memberDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 닉네임 중복 체크
        if (isNicknameDuplicate(memberDto.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(memberDto.getPassword());
        memberDto.setPassword(encryptedPassword);

        // 회원 정보 저장
        memberMapper.setSignUp(memberDto);

        System.out.println("사용자가 성공적으로 등록되었습니다.");
    }

    // 이메일 중복 여부 확인 메서드
    public boolean isEmailDuplicate(String email) {
        return memberMapper.duplicateEmail(email) > 0;
    }

    // 닉네임 중복 여부 확인 메서드
    public boolean isNicknameDuplicate(String nickname) {
        return memberMapper.duplicateNick(nickname) > 0;
    }


    // 로그인 인증 메서드
    public MemberDto verifyLogin(String email, String password) {
        MemberDto member = memberMapper.findByEmail(email);

        if (member != null && passwordEncoder.matches(password, member.getPassword())) {
            return member;
        } else {
            return null;
        }
    }



    // 로그인한 사용자 정보를 반환하는 메서드
    public MemberDto getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // 로그인한 사용자의 이메일을 가져옵니다.
        return memberMapper.findByEmail(email); // 이메일을 사용해 사용자 정보를 조회합니다.
    }

    // DB에 토큰 저장하는 메서드
    public void saveToken(int userNumber, String accessToken, String refreshToken) {
        memberMapper.saveToken(userNumber, accessToken, refreshToken);
    }

}
