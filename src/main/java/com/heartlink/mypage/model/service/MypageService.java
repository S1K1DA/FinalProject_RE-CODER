package com.heartlink.mypage.model.service;

import com.heartlink.mypage.model.dto.MypageDto;
import com.heartlink.review.model.dto.ReviewDto;

import java.util.List;

public interface MypageService {

    //유저 정보 관련
    MypageDto getUserInfo(int userId);

    String getPasswordByUserId(int userId);

    int updateUserInfo(MypageDto user);

    //유저 성향 관련
    List<MypageDto> getPersonalCategoriesByType(String type);

    List<Integer> getUserSelectedCategories(int userId);

    void saveUserCategories(int userId, List<Integer> categoryIds);

    //유저 취미 관련
    List<MypageDto> getHobbyCategories();

    void saveUserHobbies(int userId, List<Integer> hobbyIds);

    List<MypageDto> getUserHobbies(int userId);

    //유저 리뷰 관련
    List<ReviewDto> getLiveReviews(int userId);

    List<ReviewDto> getPhotoReviews(int userId);
}
