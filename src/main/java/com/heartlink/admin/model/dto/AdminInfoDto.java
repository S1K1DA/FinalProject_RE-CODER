package com.heartlink.admin.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자 .
@ToString
public class AdminInfoDto {

    private int adminUserNo;
    private String adminUserPwd;
    private String adminUserEmail;
    private String adminUserPwdConfirm;

    private String adminConsent;
}
