package com.heartlink.mypage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    @GetMapping("/main")
    public String mainpage() {
        return "mypage/mypage-main";  // "mypage/mypage-main"는 src/main/resources/templates/mypage/mypage-main.html
    }

    @GetMapping("/edit")
    public String editpage() {
        return "mypage/mypage-edit";  // "mypage/mypage-edit"는 src/main/resources/templates/mypage/mypage-edit.html
    }

    @GetMapping("/like")
    public String likepage() {
        return "mypage/mypage-like";  // "mypage/mypage-like"는 src/main/resources/templates/mypage/mypage-like.html
    }

    @GetMapping("/review")
    public String reviewpage() {
        return "mypage/mypage-review";  // "mypage/mypage-review"는 src/main/resources/templates/mypage/mypage-review.html
    }

    @GetMapping("/etc")
    public String etcpage() {
        return "mypage/mypage-etc";  // "mypage/mypage-etc"는 src/main/resources/templates/mypage/mypage-etc.html
    }
}
