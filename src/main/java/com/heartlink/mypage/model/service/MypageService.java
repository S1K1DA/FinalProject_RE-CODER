package com.heartlink.mypage.model.service;

import com.heartlink.mypage.model.dto.MypageDto;

import java.util.List;

public interface MypageService {

    // 유저 정보 관련
    MypageDto getUserInfo(int userId);

    String getPasswordByUserId(int userId);

    int updateUserInfo(MypageDto user);

    // 유저 성향 관련
    List<MypageDto> getPersonalCategoriesByType(String type);

    List<Integer> getUserSelectedCategories(int userId);

    void saveUserCategories(int userId, List<Integer> categoryIds);

    // 유저 취미 관련
    List<MypageDto> getHobbyCategories();

    void saveUserHobbies(int userId, List<Integer> hobbyIds);

    List<MypageDto> getUserHobbies(int userId);

    // 유저 리뷰 관련
    List<MypageDto> getLiveReviews(int userId);

    List<MypageDto> getPhotoReviews(int userId);

    // 리뷰 내용에서 첫 번째 이미지 URL 추출
    String extractFirstImageUrl(String content);

    //유저 상태 delete 변경
    boolean deleteUserById(int userId);

    // 좋아요한 피드 가져오기
    List<MypageDto> getLikedFeeds(int userId);

    public boolean unlikeFeed(int userId, int feedNo);

    public boolean likeFeed(int userId, int feedNo);
}
