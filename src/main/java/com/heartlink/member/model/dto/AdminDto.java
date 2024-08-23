package com.heartlink.member.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDto {
    private int adminUserNo; // 어드민 넘버
    private String email;
    private String password;
}
