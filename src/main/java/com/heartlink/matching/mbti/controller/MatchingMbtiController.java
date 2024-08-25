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
        // JWT 토큰에서 사용자 번호 추출
        int userNumber = jwtUtil.getUserNumberFromToken(jwtToken);

        // 사용자 번호로 프로필 데이터를 가져옴
        MatchingMbtiDto userProfile = matchingMbtiService.getUserProfile(userNumber);

        // 프로필 데이터를 모델에 추가
        model.addAttribute("profile", userProfile);

        // 매칭 페이지로 이동
        return "matching/mbti/matching-mbti";
    }
}
