package com.heartlink.review.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewDto {
    private int reviewNo;
    private String reviewTitle;  // Live Review에는 사용되지 않음
    private String reviewContent;
    private int reviewerUserId;
    private int reviewRating;
    private int reviewViews;
    private String firstImageUrl;  // Live Review에는 사용되지 않음
    private String reviewerNickname;
    private String reviewType;
    private String reviewDelete;  // 리뷰 삭제 여부 (Y/N)
    private String starRating;


    public void setReviewRating(int reviewRating) {
        this.reviewRating = reviewRating;
        this.starRating = "★".repeat(reviewRating) + "☆".repeat(5 - reviewRating);
    }

}
