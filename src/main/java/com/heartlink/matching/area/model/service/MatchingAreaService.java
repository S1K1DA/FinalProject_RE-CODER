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

        return userDetails;
    }

}
