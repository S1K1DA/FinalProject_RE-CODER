package com.heartlink.mypage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MainMypageController {

    @GetMapping("/main")
    public String mypage() {
        return "/mypage/mypage-main";  // "index"는 src/main/resources/templates/index.html
    }
}
