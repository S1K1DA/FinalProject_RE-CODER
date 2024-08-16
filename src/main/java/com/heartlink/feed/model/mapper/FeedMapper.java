package com.heartlink.feed.model.mapper;

import com.heartlink.feed.model.dto.FeedDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedMapper {

    public int setFeedEnroll(FeedDto feedDto);
}
