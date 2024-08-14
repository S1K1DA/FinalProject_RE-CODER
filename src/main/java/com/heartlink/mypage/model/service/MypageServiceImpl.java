package com.heartlink.mypage.model.service;

import com.heartlink.mypage.model.dao.MypageDao;
import com.heartlink.mypage.model.dto.MypageDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MypageServiceImpl implements MypageService {

    private final MypageDao mypageDao;

    public MypageServiceImpl(MypageDao mypageDao) {
        this.mypageDao = mypageDao;
    }

    //유저 정보 가져오기
    @Override
    public MypageDto getUserInfo(int userId) {
        return mypageDao.getUserInfoById(userId);
    }

    //userId의 패스워드 가져오기
    @Override
    public String getPasswordByUserId(int userId) {
        return mypageDao.getPasswordByUserId(userId);
    }

    //유저 정보 업데이트
    @Override
    public int updateUserInfo(MypageDto user) {
        return mypageDao.updateUserInfo(user);
    }

    //타입별 성향 리스트 가져오기
    @Override
    public List<MypageDto> getPersonalCategoriesByType(String type) {
        return mypageDao.getPersonalCategoriesByType(type);
    }

    //유저의 성향 리스트 가져오기
    @Override
    public List<Integer> getUserSelectedCategories(int userId) {
        return mypageDao.getUserSelectedCategories(userId);
    }

    //유저의 성향 리스트 저장
    @Override
    public void saveUserCategories(int userId, List<Integer> categoryIds) {
        mypageDao.saveUserCategories(userId, categoryIds);
    }

    //취미 리스트 가져오기
    @Override
    public List<MypageDto> getHobbyCategories() {
        return mypageDao.getHobbyCategories();
    }

    //유저 취미 리스트 저장
    @Override
    public void saveUserHobbies(int userId, List<Integer> hobbyIds) {
        mypageDao.saveUserHobbies(userId, hobbyIds);
    }

    //유저 취미 리스트 가져오기
    @Override
    public List<MypageDto> getUserHobbies(int userId) {
        return mypageDao.getUserHobbies(userId);
    }
}
