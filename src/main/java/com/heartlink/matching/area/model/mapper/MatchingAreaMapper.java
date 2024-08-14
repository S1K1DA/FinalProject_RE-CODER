package com.heartlink.matching.area.model.mapper;

import com.heartlink.matching.area.model.dto.BoundsRequestDto;
import com.heartlink.matching.area.model.dto.MatchingAreaDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MatchingAreaMapper {

    public MatchingAreaDto setUserLocation(int basicUserNo);

    public List<MatchingAreaDto> selectUserDetails(BoundsRequestDto bounds);

    public List<MatchingAreaDto> selectUserHobbies(List<Integer> userIds);

    public List<MatchingAreaDto> selectUserPersonals(List<Integer> userIds);

}
