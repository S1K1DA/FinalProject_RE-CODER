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
}
