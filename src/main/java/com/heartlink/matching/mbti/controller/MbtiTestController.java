package com.heartlink.matching.mbti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/matching")
public class MbtiTestController {


    @GetMapping("/mbti")
    public String mbtiTest() {
        return "matching/mbti-test";
    }


}
