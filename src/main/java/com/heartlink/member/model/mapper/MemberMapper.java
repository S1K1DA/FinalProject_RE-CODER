package com.heartlink.member.model.mapper;


import com.heartlink.member.model.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    // 회원가입 메서드: MemberDTO 객체를 파라미터로 받음
    public int setSignUp(@Param("memberDto") MemberDto memberDto);

    // 이메일 중복 체크 메서드: email 파라미터를 받음
    public int duplicateEmail(@Param("email") String email);

    // 닉네임 중복 체크 메서드: nickname 파라미터를 받음
    public int duplicateNick(@Param("nickname") String nickname);

    // 로그인 메서드 : email로 사용자 조회
    MemberDto findByEmail(@Param("email") String email);

    // 토큰 저장 메서드
    void saveToken(@Param("userNumber") int userNumber, @Param("accessToken") String accessToken, @Param("refreshToken") String refreshToken);

    // 사용자의 토큰 조회 메서드
    String findTokenByUserNumber(@Param("userNumber") int userNumber);
}
