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

    public int updateUserInfo(MypageDto user) {
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

    // 취미 관련 메소드들
    public List<MypageDto> getHobbyCategories() {
        return sqlSession.selectList("mypageMapper.getHobbyCategories");
    }

    public void saveUserHobbies(int userId, List<Integer> hobbyIds) {
        sqlSession.delete("mypageMapper.deleteUserHobbies", userId);

        if (hobbyIds != null && !hobbyIds.isEmpty()) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("hobbyIds", hobbyIds);
            sqlSession.insert("mypageMapper.insertUserHobbies", params);
        }
    }

    public List<MypageDto> getUserHobbies(int userId) {
        return sqlSession.selectList("mypageMapper.getUserHobbies", userId);
    }

    // 리뷰 관련 메소드들 추가
    public List<MypageDto> getReviewsByType(int userId, String reviewType) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("reviewType", reviewType);
        return sqlSession.selectList("mypageMapper.getReviewsByType", params);
    }
}
