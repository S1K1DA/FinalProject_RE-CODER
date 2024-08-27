package com.heartlink.report.model.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자 .
@ToString
public class ReportDto {

    private int reportNo;
    private int reportCategoryNo;
    private String reportCategoryName;
    private int reportedUserNo;
    private int reporterUserNo;

    private String reportIndate;
    private String reportContent;
    private String reportResponseStatus;
    private String reportType;
    private int reportTypeNo;

}
