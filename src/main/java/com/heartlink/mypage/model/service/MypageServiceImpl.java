package com.heartlink.mypage.model.service;

import com.heartlink.mypage.model.dao.MypageDao;
import com.heartlink.mypage.model.dto.MypageDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MypageServiceImpl implements MypageService {

    private final MypageDao mypageDao;

    public MypageServiceImpl(MypageDao mypageDao) {
        this.mypageDao = mypageDao;
    }

    // 유저 정보 관련 메소드
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
        return mypageDao.updateUserInfo(user);
    }

    // 유저 성향 관련 메소드
    @Override
    public List<MypageDto> getPersonalCategoriesByType(String type) {
        return mypageDao.getPersonalCategoriesByType(type);
    }

    @Override
    public List<Integer> getUserSelectedCategories(int userId) {
        return mypageDao.getUserSelectedCategories(userId);
    }

    @Override
    public void saveUserCategories(int userId, List<Integer> categoryIds) {
        mypageDao.saveUserCategories(userId, categoryIds);
    }

    // 유저 취미 관련 메소드
    @Override
    public List<MypageDto> getHobbyCategories() {
        return mypageDao.getHobbyCategories();
    }

    @Override
    public void saveUserHobbies(int userId, List<Integer> hobbyIds) {
        mypageDao.saveUserHobbies(userId, hobbyIds);
    }

    @Override
    public List<MypageDto> getUserHobbies(int userId) {
        return mypageDao.getUserHobbies(userId);
    }

    // 유저 리뷰 관련 메소드
    @Override
    public List<MypageDto> getLiveReviews(int userId) {
        List<MypageDto> liveReviews = mypageDao.getReviewsByType(userId, "L");
        for (MypageDto review : liveReviews) {
            String firstImageUrl = extractFirstImageUrl(review.getReviewContent());
            review.setFirstImageUrl(firstImageUrl != null ? firstImageUrl : "/image/mainThumbnail.jpg");
        }
        return liveReviews;
    }

    @Override
    public List<MypageDto> getPhotoReviews(int userId) {
        List<MypageDto> photoReviews = mypageDao.getReviewsByType(userId, "P");
        for (MypageDto review : photoReviews) {
            String firstImageUrl = extractFirstImageUrl(review.getReviewContent());
            review.setFirstImageUrl(firstImageUrl != null ? firstImageUrl : "/image/mainThumbnail.jpg");
        }
        return photoReviews;
    }

    // 리뷰 내용에서 첫 번째 이미지 URL 추출
    @Override
    public String extractFirstImageUrl(String content) {
        String imgTagPattern = "<img[^>]+src=[\"']([^\"']+)[\"'][^>]*>";
        Pattern pattern = Pattern.compile(imgTagPattern);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1); // 첫 번째 이미지의 URL 추출
        }
        return null;
    }

    // 유저 상태 변경 -deleted
    @Override
    public boolean deleteUserById(int userId) {
        int result = mypageDao.updateUserStatusToDeleted(userId);
        if (result > 0) {
            mypageDao.insertDeletedUser(userId);  // DELETED_USER 테이블에 삽입
            return true;
        }
        return false;
    }


    // 좋아요한 피드 목록 가져오기
    @Override
    public List<MypageDto> getLikedFeeds(int userId) {
        return mypageDao.getLikedFeeds(userId);
    }

    @Override
    public boolean unlikeFeed(int userId, int feedNo) {
        int result = mypageDao.deleteFeedLike(userId, feedNo);
        return result > 0;
    }

    @Override
    public boolean likeFeed(int userId, int feedNo) {
        int result = mypageDao.insertFeedLike(userId, feedNo);
        return result > 0;
    }

    @Override
    public List<MypageDto> getUserMatchingHistory(int userId) {
        return mypageDao.getUserMatchingHistory(userId);
    }

    // 좋아요한 프로필 목록 가져오기
    @Override
    public List<MypageDto> getLikedProfiles(int userId) {
        return mypageDao.getLikedProfiles(userId);
    }

    // 프로필 좋아요 추가
    @Override
    public boolean likeProfile(int userId, int likedUserNo) {
        return mypageDao.insertProfileLike(userId, likedUserNo) > 0;
    }

    // 프로필 좋아요 해제
    @Override
    public boolean unlikeProfile(int userId, int likedUserNo) {
        return mypageDao.deleteProfileLike(userId, likedUserNo) > 0;
    }

    //닉네임체크 -> count(*)이 0인지 확인
    @Override
    public boolean isNicknameUnique(String nickname) {
        return mypageDao.countByNickname(nickname) == 0;
    }

    @Override
    public MypageDto getFeedByNo(int feedNo) {
        return mypageDao.getFeedByNo(feedNo);
    }

    @Override
    public MypageDto getUserLocation(int userId) {
        return mypageDao.getUserLocation(userId);
    }

    @Override
    public int updateUserLocation(int userId, double latitude, double longitude) {
        return mypageDao.updateUserLocation(userId, latitude, longitude);
    }

    @Override
    public void saveUserProfilePhoto(MypageDto userPhoto) {
        mypageDao.saveUserProfilePhoto(userPhoto);
    }

    @Override
    public boolean updateMatchingState(int matchingNo, int userId, String state) {
        return mypageDao.updateMatchingState(matchingNo, userId, state) > 0;
    }


}
