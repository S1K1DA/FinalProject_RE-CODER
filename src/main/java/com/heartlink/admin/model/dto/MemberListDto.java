package com.heartlink.admin.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자 .
@ToString
public class MemberListDto {

    private int basicUserNo;
    private String basicUserEmail;
    private String basicUserNickname;
    private String basicUserTelnum;
    private String basicUserBirthdate;
    private String basicUserSex;
    private String basicUserIndate;
    private String basicUserStatus;
    private String lastLoginDate;
    private int basicUserCoin;
    private String consentLocationInfo;

    private String category;
    private String search;

}
