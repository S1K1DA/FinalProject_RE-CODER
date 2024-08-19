package com.heartlink.mypage.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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

    // 성향 관련 필드들
    private int personalNo;
    private String personalName;
    private String personalType;

    // 취미 관련 필드들
    private int hobbyNo;
    private String hobbyName;

    // 리뷰 관련 필드들 추가
    private int reviewNo;
    private int reviewedUserId;
    private int reviewerUserId;
    private Date reviewIndate;
    private Date reviewUpdate;
    private String reviewDelete;
    private int reviewRating;
    private String reviewContent;
    private int reviewViews;
    private String reviewType;
    private String reviewTitle;
    private String firstImageUrl;

    // 피드 관련 필드들 추가
    private int feedNo;
    private String feedTitle;
    private String author;
    private Date likedTime;

    // 매칭 관련 필드들 추가
    private int matchingNo;
    private Date matchingIndate;
    private String partnerName;        // 매칭 상대 이름
    private String matchingState;      // 매칭 성공 여부
    private String matchingChattingState; // 채팅 종료 여부

    // 프로필 좋아요 관련 필드들 추가
    private int likedUserNo;           // 좋아요 받은 유저 번호
    private String likedUserNickname;  // 좋아요 받은 유저 닉네임
    private Date likeIndate;           // 좋아요 등록 시간
}
