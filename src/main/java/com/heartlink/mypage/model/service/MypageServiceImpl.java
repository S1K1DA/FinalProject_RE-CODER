package com.heartlink.mypage.model.service;

import com.heartlink.mypage.model.dao.MypageDao;
import com.heartlink.mypage.model.dto.MypageDto;
import org.springframework.stereotype.Service;

@Service
public class MypageServiceImpl implements MypageService {

    private final MypageDao mypageDao;

    public MypageServiceImpl(MypageDao mypageDao) {
        this.mypageDao = mypageDao;
    }

    @Override
    public MypageDto getUserInfo(int userId) {
        return mypageDao.getUserInfoById(userId);
    }

    @Override
    public String getPasswordByUserId(int userId) {
        return mypageDao.getPasswordByUserId(userId);
    }

    @Override
    public int updateUserInfo(MypageDto user) {
        return mypageDao.updateUserInfo(user);  // 반환값을 반환하도록 수정
    }
}
