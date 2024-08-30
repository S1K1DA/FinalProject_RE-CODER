package com.heartlink.admin.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heartlink.admin.model.dto.AdminInfoDto;
import com.heartlink.admin.model.dto.MemberListDto;
import com.heartlink.admin.model.mapper.AdminMemberMapper;
import com.heartlink.member.model.dto.AdminDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class AdminMemberService {

    private final AdminMemberMapper adminMemberMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminMemberService (AdminMemberMapper adminMemberMapper,
                               BCryptPasswordEncoder passwordEncoder,
                               RestTemplate restTemplate){
        this.adminMemberMapper = adminMemberMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<MemberListDto> getAllUser(String filter, String category,String search){

        List<MemberListDto> resultUserList = adminMemberMapper.getAllUser(filter,category,search);

        for(MemberListDto item : resultUserList){

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

    public List<MemberListDto> getAllUserState(String filter, String category,String search){

        List<MemberListDto> resultUserList = adminMemberMapper.getAllUserState(filter,category,search);

        for(MemberListDto item : resultUserList){

            if(item.getBasicUserSex().equals("F")){
                item.setBasicUserSex("여성");
            }else if(item.getBasicUserSex().equals("M")){
                item.setBasicUserSex("남성");
            }
        }

        return resultUserList;
    }

    @Transactional(rollbackFor = Exception.class)  // 트랜잭션 적용
    public String setChangeUserState(MemberListDto memberListDto) {
        int changeResult = adminMemberMapper.setChangeUserState(memberListDto);

        if (changeResult != 1) {
            throw new IllegalStateException("데이터 업데이트 실패");
        }

        return "SUCCESS";
    }


    public boolean checkEmailVerifit(String adminEmail){
        return adminMemberMapper.checkEmailVerifit(adminEmail) > 0;
    }

    public int setAdminMember(AdminInfoDto adminInfoDto){

        if(checkEmailVerifit(adminInfoDto.getAdminUserEmail())){
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String encryptedPassword = passwordEncoder.encode(adminInfoDto.getAdminUserPwd());
        adminInfoDto.setAdminUserPwd(encryptedPassword);

        return adminMemberMapper.setAdminMember(adminInfoDto);
    }

    @Transactional(readOnly = true)
    public AdminInfoDto verifyAdminLogin(AdminInfoDto adminInfoDto) {

        AdminInfoDto admin = adminMemberMapper.findAdminByEmail(adminInfoDto);

        if (!Objects.isNull(admin) &&
                passwordEncoder.matches(adminInfoDto.getAdminUserPwd(),
                                                     admin.getAdminUserPwd())){
            return admin;
        }

        return null; // 로그인 실패 시 null 반환
    }

}
