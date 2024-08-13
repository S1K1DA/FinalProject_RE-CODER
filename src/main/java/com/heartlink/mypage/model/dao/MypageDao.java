package com.heartlink.mypage.model.dao;

import com.heartlink.mypage.model.dto.MypageDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<MypageDto> getPersonalCategoriesByType(String type) {
        return sqlSession.selectList("mypageMapper.getPersonalCategoriesByType", type);
    }

    public List<Integer> getUserSelectedCategories(int userId) {
        return sqlSession.selectList("mypageMapper.getUserSelectedCategories", userId);
    }

    public void saveUserCategories(int userId, List<Integer> categoryIds) {
        sqlSession.delete("mypageMapper.deleteUserCategories", userId);
        if (categoryIds != null && !categoryIds.isEmpty()) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("categoryIds", categoryIds);
            sqlSession.insert("mypageMapper.insertUserCategories", params);
        }
    }
}
