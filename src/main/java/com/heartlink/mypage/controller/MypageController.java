package com.heartlink.mypage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    @GetMapping("/main")
    public String mainpage() {
        return "mypage/mypage_main/mypage-main";
    }

    @GetMapping("/infoedit")
    public String editpage() {
        return "mypage/mypage_main/mypage-infoedit";
    }

    @GetMapping("/feedlike")
    public String feedlikepage() {
        return "mypage/mypage_feedlike/mypage-feedlike";
    }

    @GetMapping("/ptreview")
    public String ptreview() {
        return "mypage/mypage_review/mypage-ptreview";
    }

    @GetMapping("/lireview")
    public String lireview() {
        return "mypage/mypage_review/mypage-lireview";
    }

    @GetMapping("/proflike")
    public String profpage() {
        return "mypage/mypage_proflike/mypage-proflike";
    }

    @GetMapping("/match")
    public String matchpage() {
        return "mypage/mypage_match/mypage-match";
    }

    @GetMapping("/delete")
    public String deletepage() {
        return "mypage/mypage_delete/mypage-delete";
    }

    @GetMapping("/hobby")
    public String hobbypage() {
        return "mypage/mypage_hobby/mypage-hobby";
    }

    @GetMapping("/sentiedit")
    public String sentieditpage() {
        return "mypage/mypage_hobby/mypage-sentiedit";
    }

    @GetMapping("/hobbyedit")
    public String hobbyeditpage() {
        return "mypage/mypage_hobby/mypage-hobbyedit";
    }
}
