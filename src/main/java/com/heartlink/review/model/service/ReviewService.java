package com.heartlink.review.model.service;

import com.heartlink.review.model.dto.ReviewDto;
import com.heartlink.review.model.dto.ReviewPhotoDto;

import java.util.List;

public interface ReviewService {
    List<ReviewDto> getAllReviews();
    ReviewDto getReviewDetail(int reviewNo);
    boolean savePhotoReview(ReviewDto review, ReviewPhotoDto reviewPhoto);
    void increaseReviewViews(int reviewNo);
    public String getNicknameByUserId(int userId);
    public boolean updatePhotoReview(ReviewDto review);

}
