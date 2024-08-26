package com.heartlink.matching.mbti.model.mapper;

import com.heartlink.matching.mbti.model.dto.MatchingMbtiDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MatchingMbtiMapper {

    MatchingMbtiDto getUserProfileById(@Param("userNumber") int userNumber);
    
    // 유저 정보 가져오기
    String getUserMbtiById(@Param("userNumber") int userNumber);

    // 천생연분 MBTI 사용자 매칭
    List<MatchingMbtiDto> getUsersByMbti(@Param("mbti") String mbti, @Param("userSex") String userSex);

    // 상위권 MBTI 사용자 매칭
    List<MatchingMbtiDto> getUsersByTopMbtis(@Param("mbtiList") List<String> mbtiList, @Param("userSex") String userSex);
}
