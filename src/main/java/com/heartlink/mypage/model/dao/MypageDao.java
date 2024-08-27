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
        return sqlSession.selectOne("MypageMapper.getUserInfoById", userId);
    }

    public String getPasswordByUserId(int userId) {
        return sqlSession.selectOne("MypageMapper.getPasswordByUserId", userId);
    }

    public int updateUserInfo(MypageDto user) {
        return sqlSession.update("MypageMapper.updateUserInfo", user);
    }

    public List<MypageDto> getPersonalCategoriesByType(String type) {
        return sqlSession.selectList("MypageMapper.getPersonalCategoriesByType", type);
    }

    public List<Integer> getUserSelectedCategories(int userId) {
        return sqlSession.selectList("MypageMapper.getUserSelectedCategories", userId);
    }

    public void saveUserCategories(int userId, List<Integer> categoryIds) {
        sqlSession.delete("MypageMapper.deleteUserCategories", userId);

        if (categoryIds != null && !categoryIds.isEmpty()) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("categoryIds", categoryIds);
            sqlSession.insert("MypageMapper.insertUserCategories", params);
        }
    }

    // 취미 관련 메소드들
    public List<MypageDto> getHobbyCategories() {
        return sqlSession.selectList("MypageMapper.getHobbyCategories");
    }

    public void saveUserHobbies(int userId, List<Integer> hobbyIds) {
        sqlSession.delete("MypageMapper.deleteUserHobbies", userId);

        if (hobbyIds != null && !hobbyIds.isEmpty()) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("hobbyIds", hobbyIds);
            sqlSession.insert("MypageMapper.insertUserHobbies", params);
        }
    }

    public List<MypageDto> getUserHobbies(int userId) {
        return sqlSession.selectList("MypageMapper.getUserHobbies", userId);
    }

    // 리뷰 관련 메소드들 추가
    public List<MypageDto> getReviewsByType(int userId, String reviewType) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("reviewType", reviewType);
        return sqlSession.selectList("MypageMapper.getReviewsByType", params);
    }

    // 유저 상태 변경 -deleted
    public int updateUserStatusToDeleted(int userId) {
        return sqlSession.update("MypageMapper.updateUserStatusToDeleted", userId);
    }

    // 좋아요한 피드 목록 가져오기
    public List<MypageDto> getLikedFeeds(int userId) {
        return sqlSession.selectList("MypageMapper.getLikedFeeds", userId);
    }

    public int deleteFeedLike(int userId, int feedNo) {
        Map<String, Integer> params = new HashMap<>();
        params.put("userId", userId);
        params.put("feedNo", feedNo);
        return sqlSession.delete("MypageMapper.deleteFeedLike", params);
    }

    public int insertFeedLike(int userId, int feedNo) {
        Map<String, Integer> params = new HashMap<>();
        params.put("userId", userId);
        params.put("feedNo", feedNo);
        return sqlSession.insert("MypageMapper.insertFeedLike", params);
    }

    public int insertDeletedUser(int userId) {
        return sqlSession.insert("MypageMapper.insertDeletedUser", userId);
    }

    public List<MypageDto> getUserMatchingHistory(int userId) {
        return sqlSession.selectList("MypageMapper.getUserMatchingHistory", userId);
    }

    public List<MypageDto> getLikedProfiles(int userId) {
        return sqlSession.selectList("MypageMapper.getLikedProfiles", userId);
    }

    // 프로필 좋아요 추가
    public int insertProfileLike(int userId, int likedUserNo) {
        Map<String, Integer> params = Map.of("userId", userId, "likedUserNo", likedUserNo);
        return sqlSession.insert("MypageMapper.insertProfileLike", params);
    }

    // 프로필 좋아요 해제
    public int deleteProfileLike(int userId, int likedUserNo) {
        Map<String, Integer> params = Map.of("userId", userId, "likedUserNo", likedUserNo);
        return sqlSession.delete("MypageMapper.deleteProfileLike", params);
    }

    //닉네임체크
    public int countByNickname(String nickname) {
        return sqlSession.selectOne("MypageMapper.countByNickname", nickname);
    }

    //피드가져오기
    public MypageDto getFeedByNo(int feedNo) {
        return sqlSession.selectOne("MypageMapper.getFeedByNo", feedNo);
    }

    public MypageDto getUserLocation(int userId) {
        return sqlSession.selectOne("MypageMapper.getUserLocation", userId);
    }

    public int updateUserLocation(int userId, double latitude, double longitude) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        return sqlSession.update("MypageMapper.updateUserLocation", params);
    }

    // 프로필 사진 정보 저장
    public int saveUserProfilePhoto(MypageDto userPhoto) {
        // 이미 존재하는 프로필 사진이 있는지 확인
        int existingPhotoCount = sqlSession.selectOne("MypageMapper.countUserProfilePhoto", userPhoto.getUserId());

        if (existingPhotoCount > 0) {
            // 프로필 사진이 이미 존재하면 업데이트
            return sqlSession.update("MypageMapper.updateUserProfilePhoto", userPhoto);
        } else {
            // 프로필 사진이 존재하지 않으면 삽입
            return sqlSession.insert("MypageMapper.insertUserProfilePhoto", userPhoto);
        }
    }

    public int updateMatchingState(int matchingNo, int userId, String state) {
        Map<String, Object> params = new HashMap<>();
        params.put("matchingNo", matchingNo);
        params.put("state", state);
        params.put("userId", userId);
        return sqlSession.update("MypageMapper.updateMatchingState", params);
    }





}
