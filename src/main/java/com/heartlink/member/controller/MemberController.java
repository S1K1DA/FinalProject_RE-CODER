package com.heartlink.member.controller;

import com.heartlink.member.model.dto.AdminDto;
import com.heartlink.member.model.dto.MemberDto;
import com.heartlink.member.model.service.MemberService;
import com.heartlink.member.util.EmailService;
import com.heartlink.member.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/member")
public class MemberController {

    private static final Logger logger = LogManager.getLogger(MemberController.class);
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final EmailService emailService; // EmailService 필드 추가

    @Autowired
    public MemberController(MemberService memberService, JwtUtil jwtUtil, EmailService emailService) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService; // EmailService 주입
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

            logger.info("새로운 회원 userNo : " + memberDto.getUserNumber());

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
        try {
            // 1. 일반 사용자 로그인 처리
            String loginResult = memberService.verifyLogin(email, password);

            if ("success".equals(loginResult)) {
                // 로그인 성공 시 JWT 토큰 생성
                MemberDto member = memberService.findByEmail(email); // 로그인 성공 후 사용자 정보 조회
                String accessToken = jwtUtil.generateToken(member.getEmail(), member.getUserNumber());
                String refreshToken = jwtUtil.generateRefreshToken(member.getEmail(), member.getUserNumber());

                // JWT를 쿠키에 저장
                Cookie jwtCookie = new Cookie("token", accessToken);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge(86400);  // 쿠키 만료 시간 (1일)
                response.addCookie(jwtCookie);

                // DB에 토큰 저장
                memberService.saveToken(member.getUserNumber(), accessToken, refreshToken);

                return "success"; // 로그인 성공 메시지 반환
            } else if ("dormantToActive".equals(loginResult)) {
                // 휴면 계정이 풀렸을 때의 메시지 처리
                return "dormantToActive";
            }

            } catch (IllegalStateException e) {
            // 상태에 따른 메시지를 반환
            return e.getMessage();
        }

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
            jwtCookie.setMaxAge(86400);  // 쿠키 만료 시간 (1일)
            response.addCookie(jwtCookie);

            logger.info("로그인한 관리자 : " + admin.getAdminUserNo());

            return "adminSuccess"; // 어드민 로그인 성공 메시지 반환
        }

        return "failure"; // 로그인 실패 메시지 반환
    }


    // 아이디 찾기
    @PostMapping("/find-id")
    @ResponseBody
    public ResponseEntity<Map<String, String>> findIdByNameAndBirthdate(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();

        try {
            String name = request.get("name");
            String residentNumber = request.get("residentNumber");

            MemberDto member = memberService.findByNameAndBirthdate(name, residentNumber);

            if (member != null && member.getEmail() != null) {
                // 이메일이 존재하는 경우
                response.put("success", "true");
                response.put("email", member.getEmail());
            } else {
                // member가 null이거나 이메일이 없는 경우
                response.put("error", "false");
            }
        } catch (Exception e) {
            response.put("success", "false");
            response.put("message", "서버 오류가 발생했습니다.");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-reset-code")
    @ResponseBody
    public ResponseEntity<Map<String, String>> sendResetCode(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();

        try {
            String email = request.get("email");

            // 이메일이 존재하는지 검증
            MemberDto member = memberService.findByEmail(email);

            if (member != null) {
                String resetCode = emailService.sendResetCode(email);  // 인증번호 전송
                response.put("success", "true");
            }
        } catch (Exception e) {
            response.put("message", "서버 오류가 발생했습니다.");
        }

        return ResponseEntity.ok(response);
    }


    @PostMapping("/verify-reset-code")
    @ResponseBody
    public ResponseEntity<Map<String, String>> verifyResetCode(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();

        try {
            String email = request.get("email");
            String code = request.get("code");

            // 인증번호가 일치하는지 검증
            boolean isValid = emailService.verifyResetCode(email, code);

            if (isValid) {
                response.put("success", "true");
            } else {
                response.put("message", "잘못된 인증번호입니다.");
            }

        } catch (Exception e) {
            response.put("message", "서버 오류가 발생했습니다.");
        }

        return ResponseEntity.ok(response);
    }
    @PostMapping("/reset-password")
    @ResponseBody
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();

        try {
            String email = request.get("email");
            String newPassword = request.get("password");

            memberService.updatePassword(email, newPassword);
            response.put("success", "true");
 
        } catch (Exception e) {
            response.put("message", "비밀번호 변경 중 오류가 발생했습니다.");
        }

        return ResponseEntity.ok(response);
    }

}

