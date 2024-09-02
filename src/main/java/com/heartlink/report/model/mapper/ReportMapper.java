package com.heartlink.report.model.mapper;

import com.heartlink.report.model.dto.ReportDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReportMapper {

    public int setReportRequest(ReportDto reportDto);

    @Select("SELECT COUNT(*) FROM FEEDS WHERE FEED_NO = #{feedNo}")
    public int searchFeedNo(int feedNo);

    @Select("SELECT COUNT(*) FROM REVIEW_BOARD WHERE REVIEW_NO = #{reviewNo}")
    public int searchReviewNo(int reviewNo);  // Review 존재 여부 확인

    @Select("SELECT count(*) FROM REPORT_CATEGORY WHERE REPORT_CATEGORY_NO = #{categoryNo}")
    public int searchCategoryNo(int categoryNo);

    @Select("SELECT count(*) FROM BASIC_USER bu  WHERE BASIC_USER_NO = #{reportedUserNo}")
    public int searchReportedUserNo(int reportedUserNo);

    @Select("SELECT count(*) FROM BASIC_USER bu  WHERE BASIC_USER_NO = #{reporterUserNo}")
    public int searchReporterUserNo(int reporterUserNo);
}


