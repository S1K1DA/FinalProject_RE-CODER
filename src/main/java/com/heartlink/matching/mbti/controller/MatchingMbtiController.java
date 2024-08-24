package com.heartlink.matching.mbti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/matching")
public class MatchingMbtiController {

    @GetMapping("/mbti")
    public String mbtiMatching() {

        return "matching/mbti/matching-mbti";
    }
}
