package com.heartlink.matching.model.dto;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자 .
@ToString
public class MatchingAlarmDto {
    private int alarmNo;
    private int matchingNo; //fk
    private int matchingUserNo; // 수신 회원
    private String alarmMessage;
    private String alarmIndate;
}
