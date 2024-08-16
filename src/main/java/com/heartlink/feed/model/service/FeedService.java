package com.heartlink.feed.model.service;

import com.heartlink.feed.model.dto.FeedDto;
import com.heartlink.feed.model.mapper.FeedMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedService {

    private final FeedMapper feedMapper;

    @Autowired
    public FeedService(FeedMapper feedMapper){
        this.feedMapper = feedMapper;
    }

    public int setFeedEnroll(FeedDto feedDto){
        return feedMapper.setFeedEnroll(feedDto);
    }
}
