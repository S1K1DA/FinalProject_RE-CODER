package com.heartlink.matching.mbti.controller;

import com.heartlink.matching.mbti.model.dto.MatchingMbtiDto;
import com.heartlink.matching.mbti.model.service.MatchingMbtiService;
import com.heartlink.member.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/matching")
public class MatchingMbtiController {

    private final MatchingMbtiService matchingMbtiService;
    private final JwtUtil jwtUtil;

    @Autowired
    public MatchingMbtiController(MatchingMbtiService matchingMbtiService, JwtUtil jwtUtil) {
        this.matchingMbtiService = matchingMbtiService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/mbti")
    public String mbtiMatching(@CookieValue("token") String jwtToken, Model model) {
        int userNumber = jwtUtil.getUserNumberFromToken(jwtToken);

        // 사용자 프로필 가져오기
        MatchingMbtiDto userProfile = matchingMbtiService.getUserProfile(userNumber);
        model.addAttribute("profile", userProfile);

//        // 천생연분 리스트 가져오기
//        List<MatchingMbtiDto> soulmateUsers = matchingMbtiService.getRandomSoulmates(userNumber);
//        model.addAttribute("soulmateUsers", soulmateUsers);

        // 매칭 페이지로 이동
        return "matching/mbti/matching-mbti";
    }


    // 천생연분 매칭을 위한 API
    @GetMapping("/soulmates")
    @ResponseBody
    public List<MatchingMbtiDto> getSoulmates(@CookieValue("token") String jwtToken) {
        // JWT 토큰에서 사용자 번호 추출
        int userNumber = jwtUtil.getUserNumberFromToken(jwtToken);

        // 천생연분 매칭 결과 가져오기
        return matchingMbtiService.getRandomSoulmates(userNumber);
    }

    // 상위 매칭을 위한 API
    @GetMapping("/top-matches")
    @ResponseBody
    public List<MatchingMbtiDto> getTopMatches(@CookieValue("token") String jwtToken) {
        int userNumber = jwtUtil.getUserNumberFromToken(jwtToken);
        return matchingMbtiService.getTopMatches(userNumber);
    }
}
