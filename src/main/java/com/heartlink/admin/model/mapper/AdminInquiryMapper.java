package com.heartlink.admin.model.mapper;

import com.heartlink.admin.model.dto.AdminInquiryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminInquiryMapper {
    List<AdminInquiryDto> getInquiries(@Param("startDate") String startDate,
                                       @Param("endDate") String endDate,
                                       @Param("tag") String tag);
}
