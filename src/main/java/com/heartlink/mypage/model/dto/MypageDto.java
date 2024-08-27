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
    private String fullAddress;
    private String mbti;
    private int coin;

    private double latitude;
    private double longitude;

    private String consentLocationInfo;

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
    private String feedContent;
    private int likeCount;
    private String feedTag;
    private Date feedIndate;

    private Date likedTime;

    // 매칭 관련 필드들 추가
    private int matchingNo;
    private int matchingUserNo;        // 매칭 요청을 보낸 유저 번호
    private int matchedUserNo;         // 매칭 요청을 받은 유저 번호
    private Date matchingIndate;
    private String partnerName;        // 매칭 상대 이름
    private String matchingState;      // 매칭 성공 여부
    private String matchingChattingState; // 채팅 종료 여부

    // 프로필 좋아요 관련 필드들 추가
    private int likedUserNo;           // 좋아요 받은 유저 번호
    private String likedUserNickname;  // 좋아요 받은 유저 닉네임
    private Date likeIndate;           // 좋아요 등록 시간

    private String profilePicturePath; // 프로필 사진 경로
    private String profilePictureOriginalName; // 원본 파일명
    private String profilePictureName;

    public String getFullProfilePictureUrl() {
        return profilePicturePath + profilePictureName;
    }

    // 프로필 사진 관련 필드
    private int photoNo;
    private String photoOriginName;
    private String photoName;
    private String photoPath;

    private Date photoIndate;

    private String decisionHistory;
}
