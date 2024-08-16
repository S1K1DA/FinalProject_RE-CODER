package com.heartlink.feed.model.mapper;

import com.heartlink.feed.model.dto.FeedDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedMapper {

    public int setFeedEnroll(FeedDto feedDto);

    public List<FeedDto> getFeedList();

    public List<FeedDto> getCommentList(int feedDto);

    public int getLikeCount(int feedDto);
}
