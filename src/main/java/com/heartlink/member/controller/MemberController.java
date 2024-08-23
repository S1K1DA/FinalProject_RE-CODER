package com.heartlink.member.controller;

import com.heartlink.member.model.dto.AdminDto;
import com.heartlink.member.model.dto.MemberDto;
import com.heartlink.member.model.service.MemberService;
import com.heartlink.member.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Autowired
    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    // 회원가입, 로그인 페이지 이동
    @GetMapping("/sign")
    public String signInUp(Model model) {
        model.addAttribute("memberDto", new MemberDto()); // 빈 객체를 추가하여 폼 바인딩을 준비
        return "member/sign-in-up"; // templates/member/sign-in-up.html을 렌더링
    }

    // 회원가입 요청 처리
    @PostMapping("/register")
    public String registerMember(@Valid MemberDto memberDto, BindingResult bindingResult, Model model) {
        // 유효성 검사 실패 시 회원가입 페이지로 다시 이동
        if (bindingResult.hasErrors()) {
            return "member/sign-in-up";
        }

        // 비밀번호와 확인 비밀번호가 일치하지 않을 경우 처리
        if (!memberDto.isPasswordConfirmed()) {
            model.addAttribute("error", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "member/sign-in-up";
        }

        try {
            memberService.registerMember(memberDto);
            model.addAttribute("message", "회원가입이 성공적으로 완료되었습니다.");
            return "redirect:/member/sign"; // 회원가입 후 로그인 페이지로 리디렉션
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "member/sign-in-up"; // 에러 발생 시 다시 회원가입 페이지로 이동
        }
    }

    // 이메일 중복 체크 요청 처리
    @GetMapping("/check-email")
    @ResponseBody
    public String checkEmailDuplicate(@RequestParam String email) {
        if (memberService.isEmailDuplicate(email)) {
            return "이미 사용 중인 이메일입니다.";
        }
        return "사용 가능한 이메일입니다.";
    }

    // 닉네임 중복 체크 요청 처리
    @GetMapping("/check-nickname")
    @ResponseBody
    public String checkNicknameDuplicate(@RequestParam String nickname) {
        if (memberService.isNicknameDuplicate(nickname)) {
            return "이미 사용 중인 닉네임입니다.";
        }
        return "사용 가능한 닉네임입니다.";
    }


    // 로그인 요청 처리
    @PostMapping("/login")
    @ResponseBody
    public String loginMember(@RequestParam String email, @RequestParam String password, HttpServletResponse response, Model model) {
        // 1. 일반 사용자 로그인 처리
        MemberDto member = memberService.verifyLogin(email, password);

        if (member != null) {
            // 로그인 성공 시 JWT 토큰 생성 (일반 사용자)
            String accessToken = jwtUtil.generateToken(member.getEmail(), member.getUserNumber());
            String refreshToken = jwtUtil.generateRefreshToken(member.getEmail(), member.getUserNumber());

            // JWT를 쿠키에 저장
            Cookie jwtCookie = new Cookie("token", accessToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(86400);  // 쿠키 만료 시간 (예: 1일)
            response.addCookie(jwtCookie);

            // DB에 토큰 저장
            memberService.saveToken(member.getUserNumber(), accessToken, refreshToken);

            return "success"; // 로그인 성공 메시지 반환
        } else {
            // 2. 어드민 로그인 처리
            AdminDto admin = memberService.verifyAdminLogin(email, password);

            if (admin != null) {
                // 로그인 성공 시 JWT 토큰 생성 (어드민)
                String accessToken = jwtUtil.generateAdminToken(admin.getEmail(), admin.getAdminUserNo());
                String refreshToken = jwtUtil.generateRefreshToken(admin.getEmail(), admin.getAdminUserNo());

                // JWT를 쿠키에 저장
                Cookie jwtCookie = new Cookie("adminToken", accessToken);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge(86400);  // 쿠키 만료 시간 (예: 1일)
                response.addCookie(jwtCookie);

                return "adminSuccess"; // 어드민 로그인 성공 메시지 반환
            }
        }

        return "failure"; // 로그인 실패 메시지 반환
    }


//    @PostMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        // 쿠키 삭제
//        Cookie jwtCookie = new Cookie("token", null);
//        jwtCookie.setHttpOnly(true);
//        jwtCookie.setMaxAge(0);
//        jwtCookie.setPath("/");
//        response.addCookie(jwtCookie);
//
//        // SecurityContext 클리어
//        SecurityContextHolder.clearContext();
//
//        return "redirect:/"; // 메인 페이지로 리다이렉트
//    }

}

