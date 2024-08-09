package com.heartlink.member.controller;

import com.heartlink.member.model.dto.MemberDto;
import com.heartlink.member.model.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입,로그인 페이지 이동
    @GetMapping("/sign")
    public String signInUp() {
        return "member/sign-in-up"; // templates/member/sign_in_up.html을 렌더링
    }

    // 회원가입 요청 처리

    public String registerMember(MemberDto memberDto, Model model) {
        try {
            memberService.registerMember(memberDto);
            model.addAttribute("message", "회원가입이 성공적으로 완료되었습니다.");
            return "redirect:/member/sign"; // 회원가입 후 로그인 페이지로 리디렉션
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "member/sign-in-up"; // 에러 발생 시 다시 회원가입 페이지로 이동
        }
    }


}
