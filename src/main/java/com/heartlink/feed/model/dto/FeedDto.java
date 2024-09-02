package com.heartlink.feed.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자
@ToString
public class FeedDto {

    private int feedNo;
    private int authorUserNo;
    private String basicUserNickname;

    private String feedTitle;
    private String feedContent;
    private String feedIndate;
    private String feedUpdate;
    private String feedDelete;
    private String feedTag;

    private List<Integer> LikedUser;
    private List<FeedCommentDto> comments;

    private int likeCount;
}
