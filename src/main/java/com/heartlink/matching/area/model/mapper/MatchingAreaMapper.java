package com.heartlink.matching.area.model.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchingAreaMapper {

    public String setUserLocation(int basicUserNo);
}
