package com.heartlink.matching.model.mapper;

import com.heartlink.matching.model.dto.MatchingAlarmDto;
import com.heartlink.matching.model.dto.MatchingDto;
import com.heartlink.member.model.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MatchingMapper {

    @Select("SELECT MATCHING_SEQ.NEXTVAL FROM DUAL")
    int getNextMatchingNo();

    public MemberDto getUserInfo(int matchedUserNo);

    public List<MatchingDto> getMatchingData(int matchingUserNo, int matchedUserNo);

    public int setMatchingRequest(int setMatchingNo, int matchingUserNo, int matchedUserNo);

    public int setMatchingAlarm(int setMatchingNo, int matchingUserNo, int matchedUserNo, String alarmMsg);

    public List<MatchingAlarmDto> getUserAlarm(int userNo);
}
