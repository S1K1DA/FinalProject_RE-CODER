package com.heartlink.admin.model.mapper;

import com.heartlink.admin.model.dto.AdminInfoDto;
import com.heartlink.admin.model.dto.MemberListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMemberMapper {

    public List<MemberListDto> getAllUser();

    public int checkEmailVerifit (String adminEmail);

    public int setAdminMember (AdminInfoDto adminInfoDto);

    public AdminInfoDto findAdminByEmail (AdminInfoDto adminInfoDto);

}
