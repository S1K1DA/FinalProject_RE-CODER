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
}
