package com.heartlink.mypage.model.service;

import com.heartlink.mypage.model.dto.MypageDto;

import java.util.List;

public interface MypageService {

    MypageDto getUserInfo(int userId);

    String getPasswordByUserId(int userId);

    int updateUserInfo(MypageDto user);

    List<MypageDto> getPersonalCategoriesByType(String type);

    List<Integer> getUserSelectedCategories(int userId);

    void saveUserCategories(int userId, List<Integer> categoryIds);

    // 추가된 메소드들
    List<MypageDto> getHobbyCategories();

    void saveUserHobbies(int userId, List<Integer> hobbyIds);

    List<MypageDto> getUserHobbies(int userId);
}
