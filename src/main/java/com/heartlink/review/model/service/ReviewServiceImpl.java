package com.heartlink.review.model.service;

import com.heartlink.review.model.dao.ReviewDao;
import com.heartlink.review.model.dto.ReviewDto;
import com.heartlink.review.model.dto.ReviewPhotoDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;

    public ReviewServiceImpl(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    //리뷰 목록 가져오기
    @Override
    public List<ReviewDto> getAllReviews() {
        List<ReviewDto> reviews = reviewDao.getAllReviews();
        for (ReviewDto review : reviews) {
            String firstImageUrl = extractFirstImageUrl(review.getReviewContent());
            review.setFirstImageUrl(firstImageUrl != null ? firstImageUrl : "/image/mainThumbnail.jpg");
        }
        return reviews;
    }

    //리뷰의 글에서 이미지 추출
    private String extractFirstImageUrl(String content) {
        String imgTagPattern = "<img[^>]+src=[\"']([^\"']+)[\"'][^>]*>";
        Pattern pattern = Pattern.compile(imgTagPattern);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1); // 첫 번째 이미지의 URL 추출
        }
        return null;
    }

    //리뷰 상세보기
    @Override
    public ReviewDto getReviewDetail(int reviewNo) {
        return reviewDao.getReviewDetail(reviewNo); // 조회수 증가 없음
    }

    //리뷰 상세보기 + 조회수증가
    @Override
    public ReviewDto getReviewDetailWithViews(int reviewNo) {
        reviewDao.increaseReviewViews(reviewNo);
        return reviewDao.getReviewDetail(reviewNo);
    }

    //포토리뷰 저장
    @Override
    public boolean savePhotoReview(ReviewDto review, ReviewPhotoDto reviewPhoto) {
        int reviewResult = reviewDao.savePhotoReview(review);
        if (reviewResult > 0) {
            if (reviewPhoto != null) {
                String reviewNo = reviewDao.getLastReviewNo();
                reviewPhoto.setReviewNo(reviewNo);
                int photoResult = reviewDao.saveReviewPhoto(reviewPhoto);
                return photoResult > 0;
            }
            return true;
        }
        return false;
    }

    //조회수 증가
    @Override
    public void increaseReviewViews(int reviewNo) {
        reviewDao.increaseReviewViews(reviewNo);
    }

    //userId에서 닉네임을 가져옴
    @Override
    public String getNicknameByUserId(int userId) {
        return reviewDao.getNicknameByUserId(userId);
    }

    //포토리뷰 업데이트
    @Override
    public boolean updatePhotoReview(ReviewDto review) {
        return reviewDao.updatePhotoReview(review) > 0;
    }

    //리뷰 삭제
    @Override
    public boolean deleteReview(int reviewNo) {
        return reviewDao.deleteReview(reviewNo) > 0;
    }

    //라이브리뷰 저장
    @Override
    public boolean saveLiveReview(ReviewDto review) {
        return reviewDao.saveLiveReview(review) > 0;
    }

    //라이브리뷰 목록 가져오기
    @Override
    public List<ReviewDto> getLiveReviews() {
        return reviewDao.getLiveReviews();
    }


}
