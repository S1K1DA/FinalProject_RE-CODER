package com.heartlink.feed.model.mapper;

import com.heartlink.feed.model.dto.FeedCommentDto;
import com.heartlink.feed.model.dto.FeedDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedMapper {

    public int setFeedEnroll(FeedDto feedDto);

    public List<FeedDto> getFeedList(String feedTag, int start, int end, String feedArray);

    public List<FeedCommentDto> getCommentList(int feedNo);

    public int getLikeCount(int feedDto);

    public List<Integer> getLikedUser(int feedNo);

    public int setCommentEnroll(FeedCommentDto commentDto);

    public FeedDto setModifyFeed(int feedNo);

    public int setUpdateFeed(FeedDto feedDto);

    public int setCommentDelete(int commentNo);

    public int setFeedDelete(int feedNo);

    public int setFeedLike(int feedNo, int userNo);

    public int setFeedLikeCancel(int feedNo, int userNo);

    public List<FeedDto> getTopFeedList();

    public List<FeedDto> getNewFeedList();

    public FeedDto getTopFeedDetail(int feedNo);
}

