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

    // 상위 MBTI 매핑 로직
    private final static Map<String, List<String>> topMbtiMatches = Map.ofEntries(
            new SimpleEntry<>("ISTJ", List.of("ESFP", "ISFJ", "ESTJ")),
            new SimpleEntry<>("ISFJ", List.of("ESTP", "ESFP", "ISTJ")),
            new SimpleEntry<>("INFJ", List.of("ENFP", "ENTP", "INFP")),
            new SimpleEntry<>("INTJ", List.of("ENFP", "ENTJ", "INTP")),
            new SimpleEntry<>("ISTP", List.of("ESFJ", "ESTP", "INTP")),
            new SimpleEntry<>("ISFP", List.of("ESTJ", "ESFJ", "INFP")),
            new SimpleEntry<>("INFP", List.of("ENFJ", "INFJ", "ENFP")),
            new SimpleEntry<>("INTP", List.of("ENTJ", "ENTP", "INFJ")),
            new SimpleEntry<>("ESTP", List.of("ISFJ", "ESFP", "ISTP")),
            new SimpleEntry<>("ESFP", List.of("ISTJ", "ESTP", "ESFJ")),
            new SimpleEntry<>("ENFP", List.of("INFJ", "INTJ", "ENFJ")),
            new SimpleEntry<>("ENTP", List.of("INFJ", "ENFP", "INTP")),
            new SimpleEntry<>("ESTJ", List.of("ISFP", "ISTJ", "ESFJ")),
            new SimpleEntry<>("ESFJ", List.of("ISTP", "ISFP", "ESTJ")),
            new SimpleEntry<>("ENFJ", List.of("INFP", "INFJ", "ENFP")),
            new SimpleEntry<>("ENTJ", List.of("INTP", "INTJ", "ENFP"))
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

    // 상위 MBTI 매칭 결과 가져오기
    public List<MatchingMbtiDto> getTopMatches(int userNumber) {
        MatchingMbtiDto userProfile = matchingMbtiMapper.getUserProfileById(userNumber);
        String userMbti = userProfile.getMbti();
        String userSex = userProfile.getUserSex();

        List<String> topMatches = topMbtiMatches.get(userMbti);
        if (topMatches != null && !topMatches.isEmpty()) {
            // 상위 매칭 MBTI에 해당하는 이성 유저 리스트 가져오기
            List<MatchingMbtiDto> topMatchUsers = matchingMbtiMapper.getUsersByTopMbtis(topMatches, userSex);

            // 리스트를 랜덤하게 섞고 상위 10명을 선택
            Collections.shuffle(topMatchUsers);
            return topMatchUsers.stream().limit(10).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}
