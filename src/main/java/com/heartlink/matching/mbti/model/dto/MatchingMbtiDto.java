package com.heartlink.matching.mbti.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchingMbtiDto {

    private String profilePhoto;  // 프로필 사진 경로
    private String nickname;      // 사용자 닉네임
    private String mbti;          // 사용자 MBTI
    private String userSex;       // 사용자 성별 (추가된 필드)
}
