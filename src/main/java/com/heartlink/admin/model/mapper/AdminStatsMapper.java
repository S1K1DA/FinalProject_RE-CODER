package com.heartlink.admin.model.mapper;

import com.heartlink.admin.model.dto.MainStatsDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminStatsMapper {

    public int getToDayInquiry(String today);

    public int getUnprocessedReport();

    public MainStatsDto getSalesAll(String today);
}
