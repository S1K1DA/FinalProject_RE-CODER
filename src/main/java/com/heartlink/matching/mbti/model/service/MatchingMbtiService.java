package com.heartlink.matching.mbti.model.service;

import com.heartlink.matching.mbti.model.dto.MatchingMbtiDto;
import com.heartlink.matching.mbti.model.mapper.MatchingMbtiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // 천생연분 MBTI 매핑 로직
    private final static Map<String, String> mbtiSoulmates = Map.ofEntries(
            new SimpleEntry<>("ISTJ", "ESFP"),
            new SimpleEntry<>("ISFJ", "ESTP"),
            new SimpleEntry<>("INFJ", "ENFP"),
            new SimpleEntry<>("INTJ", "ENFP"),
            new SimpleEntry<>("ISTP", "ESFJ"),
            new SimpleEntry<>("ISFP", "ESTJ"),
            new SimpleEntry<>("INFP", "ENFJ"),
            new SimpleEntry<>("INTP", "ENTJ"),
            new SimpleEntry<>("ESTP", "ISFJ"),
            new SimpleEntry<>("ESFP", "ISTJ"),
            new SimpleEntry<>("ENFP", "INFJ"),
            new SimpleEntry<>("ENTP", "INFJ"),
            new SimpleEntry<>("ESTJ", "ISFP"),
            new SimpleEntry<>("ESFJ", "ISTP"),
            new SimpleEntry<>("ENFJ", "INFP"),
            new SimpleEntry<>("ENTJ", "INTP")
    );

    // 천생연분 MBTI에 해당하는 유저들을 랜덤으로 10명 가져오기
    public List<MatchingMbtiDto> getRandomSoulmates(int userNumber) {
        // 현재 사용자의 프로필 정보 가져오기
        MatchingMbtiDto userProfile = matchingMbtiMapper.getUserProfileById(userNumber);
        String userMbti = userProfile.getMbti();
        String userSex = userProfile.getUserSex();

        // 천생연분 MBTI 가져오기
        String soulmateMbti = mbtiSoulmates.get(userMbti);

        System.out.println("사용자 MBTI: " + userMbti);
        System.out.println("천생연분 MBTI: " + soulmateMbti);
        System.out.println("사용자 성별: " + userSex);

        if (soulmateMbti != null) {
            // 천생연분 MBTI에 해당하는 이성 유저 리스트 가져오기
            List<MatchingMbtiDto> soulmateUsers = matchingMbtiMapper.getUsersByMbti(soulmateMbti, userSex);

            System.out.println("매칭된 유저 수: " + soulmateUsers.size());

            // 리스트를 랜덤하게 섞고 상위 10명을 선택
            Collections.shuffle(soulmateUsers);
            return soulmateUsers.stream().limit(10).collect(Collectors.toList());
        } else {
            System.out.println("천생연분을 찾을 수 없습니다.");
            return Collections.emptyList(); // 천생연분이 없을 경우 빈 리스트 반환
        }
    }



}
