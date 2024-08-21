package com.heartlink.matching.area.model.service;

import com.heartlink.matching.area.model.dto.BoundsRequestDto;
import com.heartlink.matching.area.model.dto.MatchingAreaDto;
import com.heartlink.matching.area.model.mapper.MatchingAreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchingAreaService {
    private final MatchingAreaMapper areaMapper;

    @Autowired
    public MatchingAreaService(MatchingAreaMapper areaMapper){
        this.areaMapper = areaMapper;
    }

    public MatchingAreaDto setUserLocation(int basicUserNo){
        return areaMapper.setUserLocation(basicUserNo);
    }

    public List<MatchingAreaDto> getUserIsBounds(BoundsRequestDto bounds){

        List<MatchingAreaDto> userDetails = areaMapper.selectUserDetails(bounds);

        System.out.println(userDetails.stream().toList());

        for (MatchingAreaDto item : userDetails) {
            // consentLocationInfo 값 가져오기
            String consentLocationInfo = item.getConsentLocationInfo();

            // consentLocationInfo 값 확인하고 처리
            if ("N".equals(consentLocationInfo)) {
                item.setLatitude(0);
                item.setLongitude(0);
            }
        }

        return userDetails;
    }

}
