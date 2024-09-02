package com.heartlink.member.model.service;

import com.heartlink.member.model.dto.AdminDto;
import com.heartlink.member.model.dto.MemberDto;
import com.heartlink.member.model.mapper.MemberManagementMapper;
import com.heartlink.member.model.mapper.MemberMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
public class MemberService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;
    private final MemberManagementMapper managementMapper;

    public MemberService(BCryptPasswordEncoder passwordEncoder,
                         MemberMapper memberMapper,
                         MemberManagementMapper managementMapper) {
        this.passwordEncoder = passwordEncoder;
        this.memberMapper = memberMapper;
        this.managementMapper = managementMapper;
    }

    // 이메일로 사용자 정보 조회
    public MemberDto findByEmail(String email) {
        return memberMapper.findByEmail(email);
    }

    @Transactional(rollbackFor = Exception.class)
    public void registerMember(MemberDto memberDto) {
        // 이메일 중복 체크
        if (isEmailDuplicate(memberDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 닉네임 중복 체크
        if (isNicknameDuplicate(memberDto.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(memberDto.getPassword());
        memberDto.setPassword(encryptedPassword);

        // 회원 정보 저장
        memberMapper.setSignUp(memberDto);

        int userNo = memberMapper.setUserNo(memberDto.getEmail());
        memberDto.setUserNumber(userNo);
        memberMapper.setLocation(memberDto);


        System.out.println("사용자가 성공적으로 등록되었습니다.");
    }

    // 이메일 중복 여부 확인 메서드
    public boolean isEmailDuplicate(String email) {
        return memberMapper.duplicateEmail(email) > 0;
    }

    // 닉네임 중복 여부 확인 메서드
    public boolean isNicknameDuplicate(String nickname) {
        return memberMapper.duplicateNick(nickname) > 0;
    }


    // 로그인 인증 메서드
    public MemberDto verifyLogin(String email, String password) {
        MemberDto member = memberMapper.findByEmail(email);

        if (member != null && passwordEncoder.matches(password, member.getPassword())) {
            memberMapper.updateLastLoginDate(email);
            return member;
        } else {
            return null;
        }
    }

    // 어드민 로그인 메서드
    public AdminDto verifyAdminLogin(String email, String password) {

        AdminDto admin = memberMapper.findAdminByEmail(email);

        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }

        return null; // 로그인 실패 시 null 반환
    }

    // 로그인한 사용자 정보를 반환하는 메서드
    public MemberDto getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // 로그인한 사용자의 이메일을 가져옵니다.
        return memberMapper.findByEmail(email); // 이메일을 사용해 사용자 정보를 조회합니다.
    }

    // DB에 토큰 저장하는 메서드
    public void saveToken(int userNumber, String accessToken, String refreshToken) {
        memberMapper.saveToken(userNumber, accessToken, refreshToken);
    }



    // 스케줄러 서비스
    public String deactivateInactiveUsers(String today){

        List<Integer> dormantUserList = managementMapper.getDormantUserList(today);
        int dormantUserCnt = dormantUserList.size();    // 리스트 사이즈 (인원 수)

        int resultNum = 0;  // 몇명이 업데이트 성공했는지

        if(!Objects.isNull(dormantUserList)){
            for(int userNo : dormantUserList){
                resultNum += managementMapper.setDormantUserUpdate(userNo);
            }

            return "스캔된 휴면 계정 " +
                    dormantUserCnt +
                    "명, 최종 " +
                    resultNum +
                    "명 휴면 상태 업데이트 성공";
        }else{
            return "휴면계정 없음";
        }
    }

    public String reactivateExpiredBannedUsers(String today){

        List<Integer> bandedUserList = managementMapper.getBandedUserList(today);
        int bandedUserCnt = bandedUserList.size();    // 리스트 사이즈 (인원 수)

        int resultNum = 0;  // 몇명이 업데이트 성공했는지

        if(!Objects.isNull(bandedUserList)){
            for(int userNo : bandedUserList){
                resultNum += managementMapper.setActiveUserUpdate(userNo);
            }

            return "활동 정지 종료된 계정 " +
                    bandedUserCnt +
                    "명, 최종 " +
                    resultNum +
                    "명 활성화 상태 업데이트 성공";
        }else{
            return "활동 정지 종료된 계정 없음";
        }
    }

    public String deleteUsersMarkedAsDeleted(String today){
        List<Integer> deletedUserList = managementMapper.getDeletedUserList(today);
        int deletedUserCnt = deletedUserList.size();    // 리스트 사이즈 (인원 수)

        int resultNum = 0;  // 몇명이 업데이트 성공했는지

        if(!Objects.isNull(deletedUserList)){
            for(int userNo : deletedUserList){
                resultNum += managementMapper.setUserDelete(userNo);
            }

            return "삭제 예정 계정 " +
                    deletedUserCnt +
                    "명, 최종 " +
                    resultNum +
                    "명 삭제 성공";
        }else{
            return "삭제된 계정 없음";
        }
    }
    // 아이디 찾기
    public MemberDto findByNameAndBirthdate(String name, String residentNumber) {
        return memberMapper.findByNameAndBirthdate(name, residentNumber);
    }

    // 비밀번호 변경
    public void updatePassword(String email, String newPassword) {
        String encryptedPassword = passwordEncoder.encode(newPassword);
        memberMapper.updatePassword(email, encryptedPassword);
    }

}
