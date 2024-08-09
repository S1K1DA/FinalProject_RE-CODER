package com.heartlink.member.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    private String email;
    private String password;
    private String confirmPassword;
    private String nickname;
    private String name;
    private String juminNumber;
    private String phoneNumber;
    private String address;
}
