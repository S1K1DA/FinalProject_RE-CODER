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

    @Override
    public List<ReviewDto> getAllReviews() {
        List<ReviewDto> reviews = reviewDao.getAllReviews();
        for (ReviewDto review : reviews) {
            String firstImageUrl = extractFirstImageUrl(review.getReviewContent());
            review.setFirstImageUrl(firstImageUrl != null ? firstImageUrl : "/image/default-thumbnail.jpg");
        }
        return reviews;
    }

    private String extractFirstImageUrl(String content) {
        String imgTagPattern = "<img[^>]+src=[\"']([^\"']+)[\"'][^>]*>";
        Pattern pattern = Pattern.compile(imgTagPattern);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1); // 첫 번째 이미지의 URL 추출
        }
        return null;
    }

    @Override
    public ReviewDto getReviewDetail(int reviewNo) {
        reviewDao.increaseReviewViews(reviewNo);
        return reviewDao.getReviewDetail(reviewNo);
    }

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

    @Override
    public void increaseReviewViews(int reviewNo) {
        reviewDao.increaseReviewViews(reviewNo);
    }
}
