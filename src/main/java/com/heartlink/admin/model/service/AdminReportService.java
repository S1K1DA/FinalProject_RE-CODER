package com.heartlink.admin.model.service;

import com.heartlink.admin.model.dto.AdminReportDto;
import com.heartlink.admin.model.mapper.AdminReportMapper;
import org.springframework.stereotype.Service;

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

    public List<AdminReportDto> setReportList(){
        return adminReportMapper.setReportList();
    }

    public String setAdminResolution(AdminReportDto reportDto){

        if(reportDto.isPunisNone()){
            reportDto.setReportRsolutionPunish("해당없음");
            reportDto.setPunishmentResult("");
        }else{
            if(reportDto.isPermanentBan()){
                reportDto.setReportRsolutionPunish("영구정지");
                reportDto.setPunishmentResult("9999-99-99");
            }else {
                reportDto.setReportRsolutionPunish("정지");
            }
            int updateResult = adminReportMapper.updateResolution(reportDto);

            if(updateResult != 1){
                return "REPORT 테이블 데이터 업데이트 실패";
            }

            System.out.println("피신고자 번호 : " + reportDto.getReportedUserNo());

            int updateUserState = adminReportMapper.updateUserState(reportDto.getReportedUserNo(), "BAN");

            System.out.println("업데이트 확인 : " + updateUserState);

            if(updateUserState != 1){
                return "회원 테이블 데이터 업데이트 실패";
            }
        }

        int insertResult = adminReportMapper.setAdminResolution(reportDto);

        if(insertResult != 1){
            return "데이터 삽입 실패";
        }

        return "SUCCESS";
    }
}
