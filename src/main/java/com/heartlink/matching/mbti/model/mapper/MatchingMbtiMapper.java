package com.heartlink.matching.mbti.model.mapper;

import com.heartlink.matching.mbti.model.dto.MatchingMbtiDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MatchingMbtiMapper {

    MatchingMbtiDto getUserProfileById(@Param("userNumber") int userNumber);
}
