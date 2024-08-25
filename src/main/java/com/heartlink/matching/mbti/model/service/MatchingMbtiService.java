package com.heartlink.matching.mbti.model.service;

import com.heartlink.matching.mbti.model.dto.MatchingMbtiDto;
import com.heartlink.matching.mbti.model.mapper.MatchingMbtiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchingMbtiService {

    private final MatchingMbtiMapper matchingMbtiMapper;

    @Autowired
    public MatchingMbtiService(MatchingMbtiMapper matchingMbtiMapper) {
        this.matchingMbtiMapper = matchingMbtiMapper;
    }

    // 특정 사용자의 프로필 가져오기
    public MatchingMbtiDto getUserProfile(int userNumber) {
        return matchingMbtiMapper.getUserProfileById(userNumber);
    }
}
