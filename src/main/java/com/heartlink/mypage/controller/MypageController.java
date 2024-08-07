package com.heartlink.mypage.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    @GetMapping("/main")
    public String mainpage(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "mypage/mypage_main/mypage-main";
    }

    @GetMapping("/infoedit")
    public String editpage(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "mypage/mypage_main/mypage-infoedit";
    }

    @GetMapping("/feedlike")
    public String feedlikepage(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "mypage/mypage_feedlike/mypage-feedlike";
    }

    @GetMapping("/ptreview")
    public String ptreview(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "mypage/mypage_review/mypage-ptreview";
    }

    @GetMapping("/lireview")
    public String lireview(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "mypage/mypage_review/mypage-lireview";
    }

    @GetMapping("/proflike")
    public String profPage(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "mypage/mypage_proflike/mypage-proflike";
    }

    @GetMapping("/match")
    public String matchPage(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "mypage/mypage_match/mypage-match";
    }

    @GetMapping("/delete")
    public String deletePage(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "mypage/mypage_delete/mypage-delete";
    }

    @GetMapping("/hobby")
    public String hobbyPage(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "mypage/mypage_hobby/mypage-hobby";
    }

    @GetMapping("/sentiedit")
    public String sentiEditPage(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "mypage/mypage_hobby/mypage-sentiedit";
    }

    @GetMapping("/hobbyedit")
    public String hobbyEditPage(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "mypage/mypage_hobby/mypage-hobbyedit";
    }
}
