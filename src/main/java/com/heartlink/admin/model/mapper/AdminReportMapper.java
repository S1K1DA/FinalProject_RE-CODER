package com.heartlink.admin.model.mapper;

import com.heartlink.admin.model.dto.AdminReportDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminReportMapper {

    public List<AdminReportDto> getReportCategory();
}
