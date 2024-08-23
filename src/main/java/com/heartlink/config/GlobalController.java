package com.heartlink.config;

import com.heartlink.matching.model.dto.MatchingAlarmDto;
import com.heartlink.matching.model.service.MatchingService;
import com.heartlink.member.model.dto.MemberDto;
import com.heartlink.member.model.service.MemberService;
import com.heartlink.member.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalController {

    private final MemberService memberService;
    private final MatchingService matchingService;
    private final JwtUtil jwtUtil;

    @Autowired
    public GlobalController(MemberService memberService, MatchingService matchingService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.matchingService = matchingService;
        this.jwtUtil = jwtUtil;
    }

    // SecurityContext에서 userNo 가져오기
    private int getCurrentUserNo() {
        String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return jwtUtil.getUserNumberFromToken(jwt);
    }

    @ModelAttribute("member")
    public MemberDto addMemberToModel() {
        System.out.println("Global : ");
        return memberService.getLoggedInUser();
    }

    @ModelAttribute("alarmList")
    public List<MatchingAlarmDto> addAlarmListToModel() {
        try {
            int userNo = getCurrentUserNo();
            return matchingService.getUserAlarm(userNo);
        } catch (Exception e) {
            System.out.println("Error fetching user alarms: " + e.getMessage());
            return List.of(); // 빈 리스트 반환
        }
    }


}
