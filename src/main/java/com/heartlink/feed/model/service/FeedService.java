package com.heartlink.feed.model.service;

import com.heartlink.feed.model.dto.FeedCommentDto;
import com.heartlink.feed.model.dto.FeedDto;
import com.heartlink.feed.model.mapper.FeedMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService {

    private final FeedMapper feedMapper;

    @Autowired
    public FeedService(FeedMapper feedMapper){
        this.feedMapper = feedMapper;
    }

    public List<FeedDto> getFeedList(String tag, int start, int end){

        String feedTag = "전체";
        if (!tag.equals("전체")) {
            feedTag = tag;
        }

        System.out.println("서비스 태그 : " + tag);
        System.out.println("서비스 시작점 : " + start);
        System.out.println("서비스 끝점 : " + end);

        List<FeedDto> textList = feedMapper.getFeedList(feedTag, start, end);

        for (FeedDto feed : textList) {
            int feedNo = feed.getFeedNo();

            // 태그 다시 달아주기
            String originalContent = StringEscapeUtils.unescapeHtml4(feed.getFeedContent());
            feed.setFeedContent(originalContent);

            // 댓글 리스트 가져오기
            List<FeedCommentDto> commentList = feedMapper.getCommentList(feedNo);
            feed.setComments(commentList);

            // 좋아요 수 가져오기
            int likeCount = feedMapper.getLikeCount(feedNo);
            feed.setLikeCount(likeCount); // FeedDto 클래스에 좋아요 수를 저장할 수 있는 필드가 있어야 합니다.
        }

        return textList;
    }

    public int setFeedEnroll(FeedDto feedDto){
        return feedMapper.setFeedEnroll(feedDto);
    }

    public int setCommentEnroll(FeedCommentDto commentDto){
        return feedMapper.setCommentEnroll(commentDto);
    }

    public FeedDto setModifyFeed(int feedNo){

        FeedDto selectEdit = feedMapper.setModifyFeed(feedNo);

        String originContent = StringEscapeUtils.unescapeHtml4(selectEdit.getFeedContent());
        selectEdit.setFeedContent(originContent);

        return selectEdit;
    }

    public int setUpdateFeed(FeedDto feedDto){

        return feedMapper.setUpdateFeed(feedDto);
    }

    public int setCommentDelete(int commentNo){
        return feedMapper.setCommentDelete(commentNo);
    }

    public int setFeedDelete(int feedNo){
        return feedMapper.setFeedDelete(feedNo);
    }
}
