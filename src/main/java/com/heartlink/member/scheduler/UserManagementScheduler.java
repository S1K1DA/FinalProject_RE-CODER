package com.heartlink.member.scheduler;

import com.heartlink.member.model.service.MemberService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class UserManagementScheduler {

    private final MemberService memberService;

    private static final Logger logger = LogManager.getLogger(UserManagementScheduler.class);

    @Autowired
    public UserManagementScheduler(MemberService memberService){
        this.memberService = memberService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void performScheduledTasks() {

        LocalDate todayLocal = LocalDate.now();
        String today = todayLocal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        try {
            deactivateInactiveUsers(today);

            reactivateExpiredBannedUsers(today);

            deleteUsersMarkedAsDeleted(today);
        }catch (Exception e){
            logger.warn("UserManagementScheduler error : " + e);
        }

    }

    private void deactivateInactiveUsers(String today) {
    // 자정마다 last login 날짜를 스캔해서 6개월 동안 로그인이 없으면 휴면상태
        String result = memberService.deactivateInactiveUsers(today);

        logger.info(result);
    }
    private void reactivateExpiredBannedUsers(String today) {
    // Ban 상태인 회원의 PUNISHMENT_RESULT 컬럼을 스캔하여 날짜가 지났으면 Active 상태로 업데이트하는 로직 구현
    //  - 날짜가 지났으면 Active 상태로 업데이트
        String result = memberService.reactivateExpiredBannedUsers(today);

        logger.info(result);
    }
    private void deleteUsersMarkedAsDeleted(String today){
        // deleted 상태인 회원 정보 삭제
        String result = memberService.deleteUsersMarkedAsDeleted(today);

        logger.info(result);
    }
}