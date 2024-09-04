package com.heartlink.admin.model.service;

import com.heartlink.admin.model.dto.AdminReportDto;
import com.heartlink.admin.model.mapper.AdminReportMapper;
import com.heartlink.common.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<AdminReportDto> setResolutionReportList(String startDate, String endDate) {
        List<AdminReportDto> resolutionList = adminReportMapper.setResolutionReportList(startDate, endDate);

        for (AdminReportDto item : resolutionList) {

            String punishResult = item.getReportRsolutionPunish();

            // null 체크를 먼저 수행합니다.
            if ("정지".equals(punishResult)) {

                // 총 며칠 정지인지 계산
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                // reportIndate와 punishmentResult의 null 체크 및 예외 처리
                LocalDateTime reportIndate = null;
                LocalDateTime punishDate = null;

                try {
                    reportIndate = item.getReportIndate() != null ?
                            LocalDateTime.parse(item.getReportIndate(), formatter) : LocalDateTime.now();
                    punishDate = item.getPunishmentResult() != null ?
                            LocalDateTime.parse(item.getPunishmentResult(), formatter) : LocalDateTime.now();
                } catch (Exception e) {
                    // 파싱 오류 발생 시 기본값을 설정하거나 에러 로그를 남깁니다.
                    System.err.println("날짜 파싱 오류: " + e.getMessage());
                    reportIndate = LocalDateTime.now();  // 기본값을 설정
                    punishDate = LocalDateTime.now();
                }

                // 끝일도 포함하기 위해 +1
                Long totalDays = ChronoUnit.DAYS.between(reportIndate, punishDate) + 1;

                item.setReportRsolutionPunish(Long.toString(totalDays));
            }

            // 누적 신고 처분수
            int accruePunishment = adminReportMapper.getAccruePunishment(item.getReportedUserNo());
            item.setAccruePunishment(accruePunishment);
        }

        return resolutionList;
    }



    public List<AdminReportDto> setReportList(String startDate,String endDate){
        return adminReportMapper.setReportList(startDate, endDate);
    }

    @Transactional(rollbackFor = Exception.class)
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
                throw new CustomException("회원 테이블 데이터 업데이트 실패");
            }
        }

        int updateResult = adminReportMapper.updateResolution(reportDto);

        if(updateResult != 1){
            throw new CustomException("REPORT 테이블 데이터 업데이트 실패");
        }

        int insertResult = adminReportMapper.setAdminResolution(reportDto);

        if(insertResult != 1){
            throw new CustomException("데이터 삽입 실패");
        }

        return "SUCCESS";
    }
}
