package com.heartlink.admin.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자 .
@ToString
public class MainStatsDto {
    // 일 합계
    private int toDaySales;
    // 월 합계
    private int toMonthSales;
    // 년 합계
    private int toYearSales;

    // 이번 달
    private List<Integer> thisMonthSales;
    private List<Integer> thisMonthSalesCanceled;
    // 이번 년
    private List<Integer> thisYaerSales;
    private List<Integer> thisYaerSalesCanceled;

    // 오늘 문의 수
    private int toDayInquiryCnt;
    // 미처리 신고수
    private int unprocessedReportCnt;
}
