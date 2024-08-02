package com.heartlink.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

    @GetMapping("/sign")
    public String signInUp() {
        return "member/sign-in-up"; // templates/member/sign_in_up.html을 렌더링
    }
}
