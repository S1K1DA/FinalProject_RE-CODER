package com.heartlink.admin.model.mapper;

import com.heartlink.admin.model.dto.AdminReportDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AdminReportMapper {

    public List<AdminReportDto> getReportCategory();

    public List<AdminReportDto> setResolutionReportList(String startDate, String endDate);

    public int getAccruePunishment();

    public List<AdminReportDto> setReportList(String startDate, String endDate);

    public int setAdminResolution(AdminReportDto reportDto);

    public int updateResolution(AdminReportDto reportDto);

    public int updateUserState(@Param("reportedUserNo") int reportedUserNo,
                               @Param("state") String state);
}
