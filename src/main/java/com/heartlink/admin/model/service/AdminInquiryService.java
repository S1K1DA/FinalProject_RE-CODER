package com.heartlink.admin.model.service;

import com.heartlink.admin.model.dto.AdminInquiryDto;
import com.heartlink.admin.model.mapper.AdminInquiryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminInquiryService {
    private final AdminInquiryMapper adminInquiryMapper;

    public AdminInquiryService(AdminInquiryMapper adminInquiryMapper) {
        this.adminInquiryMapper = adminInquiryMapper;
    }

    public List<AdminInquiryDto> getInquiries(String startDate, String endDate, String tag) {
        return adminInquiryMapper.getInquiries(startDate, endDate, tag);
    }
}
