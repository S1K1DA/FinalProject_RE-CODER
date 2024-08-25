package com.heartlink.matching.mbti.model.mapper;

import com.heartlink.matching.mbti.model.dto.MatchingMbtiDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MatchingMbtiMapper {

    MatchingMbtiDto getUserProfileById(@Param("userNumber") int userNumber);

    String getUserMbtiById(@Param("userNumber") int userNumber);

    // 성별 필터링을 추가한 getUsersByMbti 메서드
    List<MatchingMbtiDto> getUsersByMbti(@Param("mbti") String mbti, @Param("userSex") String userSex);
}
