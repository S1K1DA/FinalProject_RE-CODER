package com.heartlink;

import com.heartlink.common.HomeService;
import com.heartlink.feed.model.dto.FeedDto;
import com.heartlink.matching.model.dto.MatchingAlarmDto;
import com.heartlink.matching.model.service.MatchingService;
import com.heartlink.member.model.dto.MemberDto;
import com.heartlink.member.util.JwtUtil;
import com.heartlink.review.model.dto.ReviewDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService){
        this.homeService = homeService;
    }


    @GetMapping("/")
    public String home(Model model) {

        List<ReviewDto> topViewReview = homeService.getPhotoView();
        List<FeedDto> topLikefeedList = homeService.getTopFeedList();
        List<MemberDto> topUserList = homeService.getTopUserList();

        System.out.println(topViewReview.stream().toList());
        System.out.println(topLikefeedList.stream().toList());
        System.out.println(topUserList.stream().toList());

        model.addAttribute("ReviewTop5", topViewReview);
        model.addAttribute("FeedTop", topLikefeedList);
        model.addAttribute("UserTop", topUserList);
        return "index";  // "index"는 src/main/resources/templates/admin-main.html
    }
}
