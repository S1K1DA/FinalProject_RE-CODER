package com.heartlink.mypage.model.service;

import com.heartlink.mypage.model.dto.MypageDto;

public interface MypageService {
    MypageDto getUserInfo(int userId);
    String getPasswordByUserId(int userId);
    int updateUserInfo(MypageDto user);  // 반환 타입을 void에서 int로 변경
}
