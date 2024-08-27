package com.heartlink.admin.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자 .
@ToString
public class AdminReportDto {

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

    private int adminUserNo;
    private String reportResolutionDate;
    private String reportResolutionContent;
    private String reportRsolutionPunish;   // 영구, 정지, 없음
    private String punishmentResult;    //date

    private int accruePunishment;

    private boolean permanentBan;
    private boolean punisNone;
}
