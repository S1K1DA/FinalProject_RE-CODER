package com.heartlink.feed.model.service;

import com.heartlink.feed.model.dto.FeedDto;
import com.heartlink.feed.model.mapper.FeedMapper;
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

    public List<FeedDto> getFeedList(){
        List<FeedDto> textList = feedMapper.getFeedList();

        for (FeedDto feed : textList) {
            int feedNo = feed.getFeedNo();

            // 댓글 리스트 가져오기
            List<FeedDto> commentList = feedMapper.getCommentList(feedNo);

            System.out.println(commentList.toString());

            for(FeedDto comment : commentList){
                feed.setCommentContent(comment.getCommentContent());
                feed.setCommentIndate(comment.getFeedIndate());
                feed.setCommentUserNickname(comment.getCommentUserNickname());
            }

            // 좋아요 수 가져오기
            int likeCount = feedMapper.getLikeCount(feedNo);
            feed.setLikeCount(likeCount); // FeedDto 클래스에 좋아요 수를 저장할 수 있는 필드가 있어야 합니다.
        }

//        List<FeedDto> commentList = feedMapper.getCommentList(textList);
//        List<FeedDto> resultList = feedMapper.getLikeCount(commentList);

        return textList;
    }

    public int setFeedEnroll(FeedDto feedDto){
        return feedMapper.setFeedEnroll(feedDto);
    }
}
