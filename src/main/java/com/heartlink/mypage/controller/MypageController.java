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

    @GetMapping("/infoedit")
    public String editpage() {
        return "mypage/mypage-infoedit";  // "mypage/mypage-infoedit"는 src/main/resources/templates/mypage/mypage-infoedit.html
    }

    @GetMapping("/feedlike")
    public String feedlikepage() {
        return "mypage/mypage-feedlike";  // "mypage/mypage-feedlike"는 src/main/resources/templates/mypage/mypage-feedlike.html
    }

    @GetMapping("/review")
    public String reviewpage() {
        return "mypage/mypage-review";  // "mypage/mypage-review"는 src/main/resources/templates/mypage/mypage-review.html
    }

    @GetMapping("/proflike")
    public String profpage() {
        return "mypage/mypage-proflike";  // "mypage/mypage-proflike"는 src/main/resources/templates/mypage/mypage-proflike.html
    }

    @GetMapping("/cash")
    public String cashpage() {
        return "mypage/mypage-cash";  // "mypage/mypage-cash"는 src/main/resources/templates/mypage/mypage-cash.html
    }

    @GetMapping("/match")
    public String matchpage() {
        return "mypage/mypage-match";  // "mypage/mypage-match"는 src/main/resources/templates/mypage/mypage-match.html
    }

    @GetMapping("/delete")
    public String deletepage() {
        return "mypage/mypage-delete";  // "mypage/mypage-delete"는 src/main/resources/templates/mypage/mypage-delete.html
    }

    @GetMapping("/hobby")
    public String hobbypage() {
        return "mypage/mypage-hobby";  // "mypage/mypage-hobby"는 src/main/resources/templates/mypage/mypage-hobby.html
    }
}
