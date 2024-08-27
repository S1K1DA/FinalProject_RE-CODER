package com.heartlink.admin.model.service;

import com.heartlink.admin.model.dto.MainStatsDto;
import com.heartlink.admin.model.mapper.AdminStatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Service
public class AdminStatsService {

    private final AdminStatsMapper adminStatsMapper;

    @Autowired
    public AdminStatsService (AdminStatsMapper adminStatsMapper){
        this.adminStatsMapper = adminStatsMapper;
    }

    public MainStatsDto getMainStatsResult(){

        LocalDate todayLocal = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = todayLocal.format(formatter);

        // 일 월 연 매출
        MainStatsDto result = adminStatsMapper.getSalesAll(today);

        // 오늘 문의 건수
        int todayInquiry = adminStatsMapper.getToDayInquiry(today);
        result.setToDayInquiryCnt(todayInquiry);

        // 미처리 신고수
        int unprocessedReport = adminStatsMapper.getUnprocessedReport();
        result.setUnprocessedReportCnt(unprocessedReport);

        return result;

    }

}
