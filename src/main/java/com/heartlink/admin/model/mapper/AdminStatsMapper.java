package com.heartlink.admin.model.mapper;

import com.heartlink.admin.model.dto.MainStatsDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminStatsMapper {

    public int getToDayInquiry(String today);

    public int getUnprocessedReport();

    public MainStatsDto getSalesAll(String today);

    public List<Integer> getThisMonthSales(String today, String firstDayOfMonth);
    public List<Integer> getThisMonthCanceledSales(String today, String firstDayOfMonth);

    public List<Integer> getThisYearSales(String thisYear);
    public List<Integer> getThisYearCanceledSales(String thisYear);
}
