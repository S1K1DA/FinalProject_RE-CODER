package com.heartlink.mypage.model.dao;

import com.heartlink.mypage.model.dto.MypageDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class MypageDao {

    private final SqlSession sqlSession;

    public MypageDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public MypageDto getUserInfoById(int userId) {
        return sqlSession.selectOne("mypageMapper.getUserInfoById", userId);
    }

    public String getPasswordByUserId(int userId) {
        return sqlSession.selectOne("mypageMapper.getPasswordByUserId", userId);
    }

    public int updateUserInfo(MypageDto user) {  // 반환 타입을 int로 유지
        return sqlSession.update("mypageMapper.updateUserInfo", user);
    }
}
