package com.heartlink.admin.model.service;

import com.heartlink.admin.model.dto.AdminReportDto;
import com.heartlink.admin.model.mapper.AdminReportMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AdminReportService {

    private final AdminReportMapper adminReportMapper;

    public AdminReportService(AdminReportMapper adminReportMapper){
        this.adminReportMapper = adminReportMapper;
    }

    public List<AdminReportDto> getReportCategory(){
        return  adminReportMapper.getReportCategory();
    }

    public List<AdminReportDto> setResolutionReportList(String startDate, String endDate){
        List<AdminReportDto> resolutionList = adminReportMapper.setResolutionReportList(startDate, endDate);

        for(AdminReportDto item : resolutionList){

            String punishResult = item.getReportRsolutionPunish();

            if(punishResult.equals("정지")){

                // 총 며칠 정지인지 계산
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                LocalDateTime reportIndate = LocalDateTime.parse(item.getReportIndate(), formatter);
                LocalDateTime punishDate = LocalDateTime.parse(item.getPunishmentResult(), formatter);

                // 끝일도 포함하기 위해 +1
                Long totalDays = ChronoUnit.DAYS.between(reportIndate, punishDate) + 1;

                item.setReportRsolutionPunish(Long.toString(totalDays));
            }

            // 누적 신고 처분수
            int accruePunishment = adminReportMapper.getAccruePunishment();
            item.setAccruePunishment(accruePunishment);

        }

        return resolutionList;
    }


    public List<AdminReportDto> setReportList(String startDate,String endDate){
        return adminReportMapper.setReportList(startDate, endDate);
    }

    public String setAdminResolution(AdminReportDto reportDto){

        if(reportDto.isPunisNone()){
            reportDto.setReportRsolutionPunish("해당없음");
            reportDto.setPunishmentResult("");
        }else{
            if(reportDto.isPermanentBan()){
                reportDto.setReportRsolutionPunish("영구정지");
                reportDto.setPunishmentResult("9999-12-31");
            }else {
                reportDto.setReportRsolutionPunish("정지");
            }

            System.out.println("피신고자 번호 : " + reportDto.getReportedUserNo());

            int updateUserState = adminReportMapper.updateUserState(reportDto.getReportedUserNo(), "BAN");

            System.out.println("업데이트 확인 : " + updateUserState);

            if(updateUserState != 1){
                return "회원 테이블 데이터 업데이트 실패";
            }
        }

        int updateResult = adminReportMapper.updateResolution(reportDto);

        if(updateResult != 1){
            return "REPORT 테이블 데이터 업데이트 실패";
        }

        int insertResult = adminReportMapper.setAdminResolution(reportDto);

        if(insertResult != 1){
            return "데이터 삽입 실패";
        }

        return "SUCCESS";
    }
}
