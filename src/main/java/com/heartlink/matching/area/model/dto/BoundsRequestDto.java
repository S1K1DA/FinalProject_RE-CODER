package com.heartlink.matching.area.model.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자 .
@ToString
public class BoundsRequestDto {

    private double southLat;    // 서
    private double northLat;    // 동
    private double southLng;    // 북
    private double northLng;    // 남

    private String requesterSex;

}