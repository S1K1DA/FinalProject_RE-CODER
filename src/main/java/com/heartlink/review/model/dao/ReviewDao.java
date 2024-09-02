package com.heartlink.review.model.dao;

import com.heartlink.review.model.dto.ReviewDto;
import com.heartlink.review.model.dto.ReviewPhotoDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewDao {
    private final SqlSession sqlSession;

    public ReviewDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<ReviewDto> getAllReviews() {
        return sqlSession.selectList("reviewMapper.getAllReviews");
    }

    public ReviewDto getReviewDetail(int reviewNo) {
        return sqlSession.selectOne("reviewMapper.getReviewDetail", reviewNo);
    }

    public int savePhotoReview(ReviewDto review) {
        return sqlSession.insert("reviewMapper.savePhotoReview", review);
    }

    public String getLastReviewNo() {
        return sqlSession.selectOne("reviewMapper.getLastReviewNo");
    }

    
    // Photo Review 등록
    public int saveReviewPhoto(ReviewPhotoDto reviewPhoto) {
        return sqlSession.insert("reviewMapper.saveReviewPhoto", reviewPhoto);
    }

    // 조회수 상승
    public int increaseReviewViews(int reviewNo) {
        return sqlSession.update("reviewMapper.increaseReviewViews", reviewNo);
    }
    
    // 아이디에서 닉네임 가져오기
    public String getNicknameByUserId(int userId) {
        return sqlSession.selectOne("reviewMapper.getNicknameByUserId", userId);
    }
    
    // Photo Review 수정
    public int updatePhotoReview(ReviewDto review) {
        return sqlSession.update("reviewMapper.updatePhotoReview", review);
    }

    // Review 삭제
    public int deleteReview(int reviewNo) {
        return sqlSession.update("reviewMapper.deleteReview", reviewNo);
    }

    // Live Review 저장
    public int saveLiveReview(ReviewDto review) {
        return sqlSession.insert("reviewMapper.saveLiveReview", review);
    }

    // Live Review 목록 가져오기
    public List<ReviewDto> getLiveReviews() {
        return sqlSession.selectList("reviewMapper.getLiveReviews");
    }

    // 닉네임 중복 체크 메소드
    public int checkNicknameDuplicate(String nickname) {
        return sqlSession.selectOne("mypageMapper.checkNicknameDuplicate", nickname);
    }

    public List<ReviewDto> getTopReviews(){
        return sqlSession.selectList("reviewMapper.getTopReviews");
    }


}
