package com.heartlink.matching.model.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자 .
@ToString
public class MatchingDto {

    private int matchingNo;
    private int matchingUserNo;
    private int matchedUserNo;

    private String matchingIndate;
    private String matchingState;
    private String matchingChattingState;

}
