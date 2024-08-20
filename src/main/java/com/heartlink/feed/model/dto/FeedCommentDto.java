package com.heartlink.feed.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자
@ToString
public class FeedCommentDto {
    private String commentUserNickname;
    private String commentContent;
    private String commentIndate;
    private int commentUserNo;
    private int feedNo;
    private int commentNo;
}