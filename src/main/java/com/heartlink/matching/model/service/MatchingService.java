package com.heartlink.matching.model.service;

import com.heartlink.matching.model.dto.MatchingAlarmDto;
import com.heartlink.matching.model.dto.MatchingDto;
import com.heartlink.matching.model.mapper.MatchingMapper;
import com.heartlink.member.model.dto.MemberDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MatchingService {

    private final MatchingMapper matchingMapper;

    public MatchingService(MatchingMapper matchingMapper){
        this.matchingMapper = matchingMapper;
    }

    public String setMatchingRequest(int matchingUserNo, int matchedUserNo){

        boolean verifit = true;

        // matchedUserNo가 실존하는지?
        MemberDto matchedUserInfo = matchingMapper.getUserInfo(matchedUserNo);
        // matchingUserNo가 실존하는지?
        MemberDto matchingUserInfo = matchingMapper.getUserInfo(matchingUserNo);

        // 두 객체의 번호가 일치하지 않고, 서로 다른 성별이어야함
        if(matchingUserInfo.getGender().equals(matchedUserInfo.getGender())
                || matchingUserNo == matchedUserNo){
            verifit = false;
            return "성별이 동일하거나 송수신자의 번호가 같습니다";
        }

        // 두 객체의 번호로 이미 매칭된 데이터가 있는지? 있다면 종료되었는지?
        List<MatchingDto> checkMathcingData = matchingMapper.getMatchingData(matchingUserInfo.getUserNumber(),
                                                                       matchedUserInfo.getUserNumber());
        if(!checkMathcingData.isEmpty()){
            for (MatchingDto item : checkMathcingData){
                if (item.getMatchingChattingState().equals("N")) {
                    return "매칭이 이미 진행중입니다";
                }
            }
        }


        // 시퀀스 번호 조회해옴
        int setMatchingNo = matchingMapper.getNextMatchingNo();

        int setMatchingRequest = matchingMapper.setMatchingRequest(setMatchingNo, matchingUserNo, matchedUserNo);
        // 알람 메세지 세팅
        String alarmMsg = matchingUserInfo.getNickname() + "님께서 " + matchedUserInfo.getNickname()+ "님께 매칭을 요청하셨습니다.";

        if(setMatchingRequest == 1){
            int setMatchingAlarm = matchingMapper.setMatchingAlarm(setMatchingNo, matchingUserNo, matchedUserNo, alarmMsg);

            if(setMatchingAlarm == 1){
                return "success";
            }
        }

        return "error";
    }

    public List<MatchingAlarmDto> getUserAlarm(int userNo){
        return matchingMapper.getUserAlarm(userNo);
    }
}
