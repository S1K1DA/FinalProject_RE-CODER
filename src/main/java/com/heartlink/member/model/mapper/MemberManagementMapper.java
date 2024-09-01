package com.heartlink.member.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberManagementMapper {

    public List<Integer> getDormantUserList(String today);

    public int setDormantUserUpdate(int userNo);

    public List<Integer> getBandedUserList(String today);

    public int setActiveUserUpdate(int userNo);

    public List<Integer> getDeletedUserList(String today);

    public int setUserDelete(int userNo);

}
