package com.heartlink.mypage.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MypageDto {
    private int userId;
    private String email;
    private String password;
    private String nickname;
    private String name;
    private String phone;
    private String address;
    private String mbti;

    private int personalNo;
    private String personalName;
    private String personalType;
}