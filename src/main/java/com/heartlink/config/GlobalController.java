package com.heartlink.config;

import com.heartlink.admin.model.dto.AdminReportDto;
import com.heartlink.admin.model.service.AdminReportService;
import com.heartlink.matching.model.dto.MatchingAlarmDto;
import com.heartlink.matching.model.service.MatchingService;
import com.heartlink.member.model.dto.MemberDto;
import com.heartlink.member.model.service.MemberService;
import com.heartlink.member.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalController {

    private final MemberService memberService;
    private final MatchingService matchingService;
    private final JwtUtil jwtUtil;
    private final AdminReportService adminReportService;

    @Autowired
    public GlobalController(MemberService memberService,
                            MatchingService matchingService,
                            AdminReportService adminReportService,
                            JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.matchingService = matchingService;
        this.adminReportService = adminReportService;
        this.jwtUtil = jwtUtil;
    }

    // SecurityContext에서 userId 가져오기
    private int getCurrentUserNo() {
        try {
            String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            return jwtUtil.getUserNumberFromToken(jwt);
        } catch (Exception e) {
            return 0;  // 로그인하지 않은 경우 0 반환
        }
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

    @ModelAttribute("reportCategory")
    public List<AdminReportDto> setReportCategory(){
        try {
            return adminReportService.getReportCategory();
        } catch(Exception e) {
            System.out.println("Error fetching user alarms: " + e.getMessage());
            return List.of(); // 빈 리스트 반환
        }
    }

    @ModelAttribute("userId")
    public Integer addUserIdToModel() {
        try {
            return getCurrentUserNo();  // getCurrentUserNo()를 사용하여 userNo를 모델에 추가
        } catch (Exception e) {
            System.out.println("Error fetching user ID: " + e.getMessage());
            return null; // 오류가 발생하면 null 반환
        }
    }


}
