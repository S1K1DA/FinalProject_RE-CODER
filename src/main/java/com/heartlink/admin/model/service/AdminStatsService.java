package com.heartlink.admin.model.service;

import com.heartlink.admin.model.dto.MainStatsDto;
import com.heartlink.admin.model.mapper.AdminStatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdminStatsService {

    private final AdminStatsMapper adminStatsMapper;

    @Autowired
    public AdminStatsService (AdminStatsMapper adminStatsMapper){
        this.adminStatsMapper = adminStatsMapper;
    }

    public MainStatsDto getMainStatsResult(){

        LocalDate todayLocal = LocalDate.now();
        // 오늘
        String today = todayLocal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 오늘 기준 이번 달 1일
        LocalDate firstDayOfMonthLocl = todayLocal.withDayOfMonth(1);
        String firstDayOfMonth = firstDayOfMonthLocl.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 이번년
        String thisYear = todayLocal.format(DateTimeFormatter.ofPattern("yyyy"));

        // 일 월 연 매출
        MainStatsDto result = adminStatsMapper.getSalesAll(today);

        // 오늘 문의 건수
        int todayInquiry = adminStatsMapper.getToDayInquiry(today);
        result.setToDayInquiryCnt(todayInquiry);

        // 미처리 신고수
        int unprocessedReport = adminStatsMapper.getUnprocessedReport();
        result.setUnprocessedReportCnt(unprocessedReport);

        // 이번 달
        List<Integer> thisMonthSales = adminStatsMapper.getThisMonthSales(today, firstDayOfMonth);
        List<Integer> thisMonthCanceledSales = adminStatsMapper.getThisMonthCanceledSales(today, firstDayOfMonth);

        result.setThisMonthSales(thisMonthSales);
        result.setThisMonthSalesCanceled(thisMonthCanceledSales);

        // 이번 년
        List<Integer> thisYearSales = adminStatsMapper.getThisYearSales(thisYear);
        List<Integer> thisYearCanceledSales = adminStatsMapper.getThisYearCanceledSales(thisYear);

        result.setThisYaerSales(thisYearSales);
        result.setThisYaerSalesCanceled(thisYearCanceledSales);

        return result;

    }

}
