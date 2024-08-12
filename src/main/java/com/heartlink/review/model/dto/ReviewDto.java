package com.heartlink.review.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewDto {
    private int reviewNo;
    private String reviewTitle;
    private String reviewContent;
    private int reviewerUserId;
    private int reviewRating;
    private int reviewViews;
    private String firstImageUrl;
    private String reviewerNickname;
}