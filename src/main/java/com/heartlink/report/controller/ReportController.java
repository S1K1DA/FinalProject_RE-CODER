package com.heartlink.report.controller;

import com.heartlink.member.util.JwtUtil;
import com.heartlink.report.model.dto.ReportDto;
import com.heartlink.report.model.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final JwtUtil jwtUtil;

    @Autowired
    public ReportController(ReportService reportService, JwtUtil jwtUtil){
        this.reportService = reportService;
        this.jwtUtil = jwtUtil;
    }

    private int getCurrentUserNo() {
        String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return jwtUtil.getUserNumberFromToken(jwt);
    }

    @PostMapping("/request")
    public ResponseEntity<?> setReportRequest(ReportDto reportDto){
        // 신고 유형은 Feed, Profile, chatting 세가지

        int reporterNo = getCurrentUserNo();
        reportDto.setReporterUserNo(reporterNo);

        String resultRequest = reportService.setReportRequest(reportDto);

        if(resultRequest.equals("SUCCESS")){
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");

    }

}
