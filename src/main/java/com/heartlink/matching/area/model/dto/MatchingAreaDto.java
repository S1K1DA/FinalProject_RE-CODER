package com.heartlink.matching.area.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String basicUserSex;

    private String hobbyName;
    private String personalLike;
    private String personalHate;

    private String photoName;
    private String photoOriginName;
    private String photoIndate;
    private String photoPath;

    private String consentLocationInfo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "###.######")
    private double latitude; //위도
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "###.######")
    private double longitude; //경도

}
