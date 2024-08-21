package com.heartlink.admin.model.service;

import com.heartlink.admin.model.dto.AdminMemberDto;
import com.heartlink.admin.model.mapper.AdminMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminMemberService {

    private final AdminMemberMapper memberMapper;

    @Autowired
    public AdminMemberService (AdminMemberMapper memberMapper){
        this.memberMapper = memberMapper;
    }

    public List<AdminMemberDto> getAllUser(){

        List<AdminMemberDto> resultUserList = memberMapper.getAllUser();

        for(AdminMemberDto item : resultUserList){

            String replaceTelnum = item.getBasicUserTelnum().replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
            item.setBasicUserTelnum(replaceTelnum);

            String replaceBirthdate = item.getBasicUserBirthdate().split(" ")[0];
            item.setBasicUserBirthdate(replaceBirthdate);

            if(item.getBasicUserSex().equals("F")){
                item.setBasicUserSex("여성");
            }else if(item.getBasicUserSex().equals("M")){
                item.setBasicUserSex("남성");
            }
        }

        return resultUserList;
    }
}
