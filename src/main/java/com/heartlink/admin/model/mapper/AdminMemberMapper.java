package com.heartlink.admin.model.mapper;

import com.heartlink.admin.model.dto.AdminMemberDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMemberMapper {

    public List<AdminMemberDto> getAllUser();
}
