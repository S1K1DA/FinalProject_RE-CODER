package com.heartlink.board.controller;

import com.heartlink.member.model.dto.MemberDto;
import com.heartlink.member.model.service.MemberService;
import com.heartlink.member.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notices")
public class NoticeController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Autowired
    public NoticeController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    // 공지사항 리스트 페이지
    @GetMapping("/list")
    public String getNoticeList(@CookieValue(value = "token", required = false) String token, Model model) {
        if (token != null) {
            // JWT 토큰에서 이메일 추출
            String email = jwtUtil.getEmailFromToken(token);
            int userNumber = jwtUtil.getUserNumberFromToken(token);

            // 이메일로 사용자 정보 조회
            MemberDto member = memberService.findByEmail(email);

            System.out.println("Notice email : " + email);
            System.out.println("Notice email : " + userNumber);

            // 사용자 정보를 모델에 추가하여 View로 전달
            model.addAttribute("member", member);
        }

        return "board/notice/notice-list";
    }



    // 공지사항 상세보기 페이지
    @GetMapping("/detail")
    public String getNoticeDetail() {
        return "board/notice/notice-detail";
    }

    // 공지사항 글쓰기 페이지
    @GetMapping("/new")
    public String createNoticeForm() {
        return "board/notice/notice-write";
    }

    // 공지사항 수정 페이지
    @GetMapping("/edit")
    public String editNoticeForm() {
        return "board/notice/notice-edit";
    }
}