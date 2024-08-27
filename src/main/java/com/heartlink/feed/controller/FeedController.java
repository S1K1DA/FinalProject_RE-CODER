package com.heartlink.feed.controller;

import com.heartlink.feed.model.dto.FeedCommentDto;
import com.heartlink.feed.model.dto.FeedDto;
import com.heartlink.feed.model.service.FeedService;
import com.heartlink.member.util.JwtUtil;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;
    private final JwtUtil jwtUtil;

    private int scrollFeedCnt;

    @Autowired
    public FeedController (FeedService feedService, JwtUtil jwtUtil){
        this.feedService = feedService;
        this.jwtUtil = jwtUtil;
    }

    // SecurityContext에서 userId 가져오기
    private int getCurrentUserNo() {
        String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return jwtUtil.getUserNumberFromToken(jwt);
    }


    @GetMapping("")
    public String moveMain(@RequestParam(value = "filter", defaultValue = "전체")String filter ,
                           @RequestParam(value = "feedarray", defaultValue = "작성일") String feedArray,
                           @RequestParam(value = "start", defaultValue = "0") int start,
                           Model model) {

        int pageSize = 5; // 페이지 당 데이터 수
        int end = start + pageSize;

        List<FeedDto> feedList = feedService.getFeedList(filter, start, end, feedArray);

        model.addAttribute("filter", filter);
        model.addAttribute("feedArray", feedArray);
        model.addAttribute("feedList", feedList);
        model.addAttribute("start", end); // 클라이언트에게 다음 요청 시 사용할 시작 페이지 번호 전달

        return "feed/feed-main";
    }

    @GetMapping("/write")
    public String moveNewFeed(){
        return "feed/feed-write";
    }

    @PostMapping("/enroll")
    public String setFeedEnroll(FeedDto feedDto){
        int userNo = getCurrentUserNo();
        feedDto.setAuthorUserNo(userNo);

        String escapeContent = StringEscapeUtils.escapeHtml4(feedDto.getFeedContent());
        feedDto.setFeedContent(escapeContent);

        int feedEnroll = feedService.setFeedEnroll(feedDto);

        if(feedEnroll != 1){
            return "common/error";
        }

        return "redirect:/feed";
    }

    @PostMapping("/addcomment")
    public ResponseEntity<?> setFeedCommentEnroll(@RequestBody FeedCommentDto commentDto){

        int commentEnroll = feedService.setCommentEnroll(commentDto);

        if(commentEnroll != 1){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database 데이터 삽입 실패");
        }

        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    @GetMapping("/modify")
    public String moveFeedModify(@RequestParam("feedNo") int feedNo, Model model){

        FeedDto selectFeed = feedService.setModifyFeed(feedNo);

        model.addAttribute("feed",selectFeed);

        return "feed/feed-modify";
    }

    @PostMapping("/modifiedcomplete")
    public String setModifiedFeed(FeedDto feedDto){

        String escapeContent = StringEscapeUtils.escapeHtml4(feedDto.getFeedContent());
        feedDto.setFeedContent(escapeContent);

        int updateFeed = feedService.setUpdateFeed(feedDto);

        return "redirect:/feed";
    }

    @PostMapping("/deletecomment")
    public ResponseEntity<?> setCommentDelete(@RequestBody FeedCommentDto commentDto){

        int commentNo = commentDto.getCommentNo();
        int deleteComment = feedService.setCommentDelete(commentNo);

        if(deleteComment != 1){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database 데이터 삽입 실패");
        }

        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    @PostMapping("/delete")
    public String setFeedDelete(@RequestBody FeedDto feedDto){

        int feedNo = feedDto.getFeedNo();

        int deleteFeed = feedService.setFeedDelete(feedNo);

        return "redirect:/feed";
    }

    @GetMapping("/reload")
    public ResponseEntity<?> reloadFeed(@RequestParam("filter") String filter,
                                        @RequestParam(value = "feedarray", defaultValue = "작성일") String feedArray,
                                        @RequestParam("page") int page) {
        int pageSize = 5; // 페이지당 데이터 수
        List<FeedDto> feedList;

        // 페이지 번호와 페이지 크기를 사용하여 데이터 범위를 계산합니다.
        int start = (page - 1) * pageSize + 1;
        int end = page * pageSize;

        feedList = feedService.getFeedList(filter, start, end, feedArray);

        // 데이터가 없으면 hasMoreData를 false로 설정할 수 있습니다.
        boolean hasMoreData = feedList.size() == pageSize;

        // 추가 정보를 응답에 포함할 수 있습니다.
        Map<String, Object> response = new HashMap<>();
        response.put("data", feedList);
        response.put("hasMoreData", hasMoreData);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/like")
    public ResponseEntity<?> setLikeFeed(@RequestParam("feedNo")int feedNo){
        int userNo = getCurrentUserNo();

        int userLikeFeed = feedService.setFeedLike(feedNo, userNo);

        if(userLikeFeed != 1){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().build();
    }

}

