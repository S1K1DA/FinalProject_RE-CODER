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

    public int saveReviewPhoto(ReviewPhotoDto reviewPhoto) {
        return sqlSession.insert("reviewMapper.saveReviewPhoto", reviewPhoto);
    }

    public int increaseReviewViews(int reviewNo) {
        return sqlSession.update("reviewMapper.increaseReviewViews", reviewNo);
    }
}
