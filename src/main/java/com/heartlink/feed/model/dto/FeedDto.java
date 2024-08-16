package com.heartlink.feed.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자
@ToString
public class FeedDto {

    private int feedNo;
    private int aouthorUserNo;
    private String basicUserNickname;

    private String feedTitle;
    private String feedContent;
    private String feedIndate;
    private String feedUpdate;
    private String feedState;
    private String feedTag;

    private String commentUserNickname;
    private String commentContent;
    private String commentIndate;

    private int likeCount;
}
