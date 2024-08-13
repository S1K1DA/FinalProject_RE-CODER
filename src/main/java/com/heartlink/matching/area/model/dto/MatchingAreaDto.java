package com.heartlink.matching.area.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자 .
@ToString
public class MatchingAreaDto {

    private int basicUserNo;
    private String userAddr;
    private String basicUserMbti;
    private String basicUserNickname;
    private String basicUserName;
    private String basicUserBirthdate;

    private String hobbyName;
    private String personalName;


}
