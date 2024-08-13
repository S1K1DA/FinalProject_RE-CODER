package com.heartlink.matching.area.model.service;

import com.heartlink.matching.area.model.mapper.MatchingAreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchingAreaService {
    private final MatchingAreaMapper areaMapper;

    @Autowired
    public MatchingAreaService(MatchingAreaMapper areaMapper){
        this.areaMapper = areaMapper;
    }

    public String setUserLocation(int basicUserNo){
        return areaMapper.setUserLocation(basicUserNo);
    }

}
