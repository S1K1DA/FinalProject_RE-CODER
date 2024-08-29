package com.heartlink.report.model.service;

import com.heartlink.report.model.dto.ReportDto;
import com.heartlink.report.model.mapper.ReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final ReportMapper reportMapper;

    @Autowired
    public ReportService(ReportMapper reportMapper){
        this.reportMapper = reportMapper;
    }

    public String setReportRequest(ReportDto reportDto){

        String reportType = reportDto.getReportType();

        // 검증
        String verifit = "";

        if(reportType.equals("Feed")){
            verifit = setTypeFeed(reportDto);
        }else if (reportType.equals("Chatting")){
            verifit = setTypeChatting(reportDto);
        }else if (reportType.equals("Profile")){
            verifit = setTypeProfile(reportDto);
        }else if (reportType.equals("Review")){
            verifit = setTypeReview(reportDto);
        }

        if(verifit.equals("verified")){
            int reportRequest = reportMapper.setReportRequest(reportDto);

            if(reportRequest == 1){
                return "SUCCESS";
            }
        }

        return "";
    }

    // feed 검증메서드
    private String setTypeFeed(ReportDto reportDto){
        // 존재하는 feed 인가?
        int feedNoCheck = reportMapper.searchFeedNo(reportDto.getReportTypeNo());

        // 존재하는 카테고리인가?
        int categoryNoCheck = reportMapper.searchCategoryNo(reportDto.getReportCategoryNo());

        // 신고대상자가 유효한가?
        int reportedUserCheck = reportMapper.searchReportedUserNo(reportDto.getReportedUserNo());

        // 신고자가 유효한가?
        int reporterUserCheck = reportMapper.searchReporterUserNo(reportDto.getReporterUserNo());

        if(feedNoCheck != 1 || categoryNoCheck != 1 ||
                reportedUserCheck != 1 || reporterUserCheck != 1){
            return "failed";
        }
        return "verified";
    }


    // Review 검증 메서드 추가
    private String setTypeReview(ReportDto reportDto){
        // 존재하는 review 인가?
        int reviewNoCheck = reportMapper.searchReviewNo(reportDto.getReportTypeNo());

        // 존재하는 카테고리인가?
        int categoryNoCheck = reportMapper.searchCategoryNo(reportDto.getReportCategoryNo());

        // 신고대상자가 유효한가?
        int reportedUserCheck = reportMapper.searchReportedUserNo(reportDto.getReportedUserNo());

        // 신고자가 유효한가?
        int reporterUserCheck = reportMapper.searchReporterUserNo(reportDto.getReporterUserNo());

        if(reviewNoCheck != 1 || categoryNoCheck != 1 ||
                reportedUserCheck != 1 || reporterUserCheck != 1){
            return "failed";
        }
        return "verified";
    }


    // 채팅 검증 메서드
    private String setTypeChatting(ReportDto reportDto){

        return "";
    }

    // 프로필 검증 메서드
    private String setTypeProfile(ReportDto reportDto){

        return "";
    }
}
