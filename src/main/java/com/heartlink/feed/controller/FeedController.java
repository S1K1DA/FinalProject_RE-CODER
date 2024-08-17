package com.heartlink.feed.controller;

import com.heartlink.feed.model.dto.FeedDto;
import com.heartlink.feed.model.service.FeedService;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Controller
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;

    @Autowired
    public FeedController (FeedService feedService){
        this.feedService = feedService;
    }


    @GetMapping("")
    public String moveMain(Model model) {

        List<FeedDto> feedList = feedService.getFeedList();
        model.addAttribute("feedList", feedList);

        return "feed/feed-main";
    }

    @GetMapping("/write")
    public String moveNewFeed(){
        return "feed/feed-write";
    }

    @PostMapping("/enroll")
    public String setFeedEnroll(FeedDto feedDto){
        String escapeContent = StringEscapeUtils.escapeHtml4(feedDto.getFeedContent());

        feedDto.setAouthorUserNo(1);
        feedDto.setFeedContent(escapeContent);

        int feedEnroll = feedService.setFeedEnroll(feedDto);

        if(feedEnroll != 1){
            return "common/error";
        }

        return "feed/feed-main";
    }
}
